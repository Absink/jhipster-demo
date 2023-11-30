import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParking, NewParking } from '../parking.model';

export type PartialUpdateParking = Partial<IParking> & Pick<IParking, 'id'>;

type RestOf<T extends IParking | NewParking> = Omit<T, 'date_creation'> & {
  date_creation?: string | null;
};

export type RestParking = RestOf<IParking>;

export type NewRestParking = RestOf<NewParking>;

export type PartialUpdateRestParking = RestOf<PartialUpdateParking>;

export type EntityResponseType = HttpResponse<IParking>;
export type EntityArrayResponseType = HttpResponse<IParking[]>;

@Injectable({ providedIn: 'root' })
export class ParkingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parkings');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(parking: NewParking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking);
    return this.http
      .post<RestParking>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(parking: IParking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking);
    return this.http
      .put<RestParking>(`${this.resourceUrl}/${this.getParkingIdentifier(parking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(parking: PartialUpdateParking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking);
    return this.http
      .patch<RestParking>(`${this.resourceUrl}/${this.getParkingIdentifier(parking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestParking>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParking[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParkingIdentifier(parking: Pick<IParking, 'id'>): number {
    return parking.id;
  }

  compareParking(o1: Pick<IParking, 'id'> | null, o2: Pick<IParking, 'id'> | null): boolean {
    return o1 && o2 ? this.getParkingIdentifier(o1) === this.getParkingIdentifier(o2) : o1 === o2;
  }

  addParkingToCollectionIfMissing<Type extends Pick<IParking, 'id'>>(
    parkingCollection: Type[],
    ...parkingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parkings: Type[] = parkingsToCheck.filter(isPresent);
    if (parkings.length > 0) {
      const parkingCollectionIdentifiers = parkingCollection.map(parkingItem => this.getParkingIdentifier(parkingItem)!);
      const parkingsToAdd = parkings.filter(parkingItem => {
        const parkingIdentifier = this.getParkingIdentifier(parkingItem);
        if (parkingCollectionIdentifiers.includes(parkingIdentifier)) {
          return false;
        }
        parkingCollectionIdentifiers.push(parkingIdentifier);
        return true;
      });
      return [...parkingsToAdd, ...parkingCollection];
    }
    return parkingCollection;
  }

  protected convertDateFromClient<T extends IParking | NewParking | PartialUpdateParking>(parking: T): RestOf<T> {
    return {
      ...parking,
      date_creation: parking.date_creation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restParking: RestParking): IParking {
    return {
      ...restParking,
      date_creation: restParking.date_creation ? dayjs(restParking.date_creation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestParking>): HttpResponse<IParking> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestParking[]>): HttpResponse<IParking[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
