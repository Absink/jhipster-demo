import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParking2, NewParking2 } from '../parking-2.model';

export type PartialUpdateParking2 = Partial<IParking2> & Pick<IParking2, 'id'>;

type RestOf<T extends IParking2 | NewParking2> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestParking2 = RestOf<IParking2>;

export type NewRestParking2 = RestOf<NewParking2>;

export type PartialUpdateRestParking2 = RestOf<PartialUpdateParking2>;

export type EntityResponseType = HttpResponse<IParking2>;
export type EntityArrayResponseType = HttpResponse<IParking2[]>;

@Injectable({ providedIn: 'root' })
export class Parking2Service {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parking-2-s');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(parking2: NewParking2): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking2);
    return this.http
      .post<RestParking2>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(parking2: IParking2): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking2);
    return this.http
      .put<RestParking2>(`${this.resourceUrl}/${this.getParking2Identifier(parking2)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(parking2: PartialUpdateParking2): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(parking2);
    return this.http
      .patch<RestParking2>(`${this.resourceUrl}/${this.getParking2Identifier(parking2)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestParking2>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParking2[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParking2Identifier(parking2: Pick<IParking2, 'id'>): number {
    return parking2.id;
  }

  compareParking2(o1: Pick<IParking2, 'id'> | null, o2: Pick<IParking2, 'id'> | null): boolean {
    return o1 && o2 ? this.getParking2Identifier(o1) === this.getParking2Identifier(o2) : o1 === o2;
  }

  addParking2ToCollectionIfMissing<Type extends Pick<IParking2, 'id'>>(
    parking2Collection: Type[],
    ...parking2sToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parking2s: Type[] = parking2sToCheck.filter(isPresent);
    if (parking2s.length > 0) {
      const parking2CollectionIdentifiers = parking2Collection.map(parking2Item => this.getParking2Identifier(parking2Item)!);
      const parking2sToAdd = parking2s.filter(parking2Item => {
        const parking2Identifier = this.getParking2Identifier(parking2Item);
        if (parking2CollectionIdentifiers.includes(parking2Identifier)) {
          return false;
        }
        parking2CollectionIdentifiers.push(parking2Identifier);
        return true;
      });
      return [...parking2sToAdd, ...parking2Collection];
    }
    return parking2Collection;
  }

  protected convertDateFromClient<T extends IParking2 | NewParking2 | PartialUpdateParking2>(parking2: T): RestOf<T> {
    return {
      ...parking2,
      dateCreation: parking2.dateCreation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restParking2: RestParking2): IParking2 {
    return {
      ...restParking2,
      dateCreation: restParking2.dateCreation ? dayjs(restParking2.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestParking2>): HttpResponse<IParking2> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestParking2[]>): HttpResponse<IParking2[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
