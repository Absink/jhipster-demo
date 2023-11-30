import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicule2, NewVehicule2 } from '../vehicule-2.model';

export type PartialUpdateVehicule2 = Partial<IVehicule2> & Pick<IVehicule2, 'id'>;

export type EntityResponseType = HttpResponse<IVehicule2>;
export type EntityArrayResponseType = HttpResponse<IVehicule2[]>;

@Injectable({ providedIn: 'root' })
export class Vehicule2Service {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicule-2-s');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(vehicule2: NewVehicule2): Observable<EntityResponseType> {
    return this.http.post<IVehicule2>(this.resourceUrl, vehicule2, { observe: 'response' });
  }

  update(vehicule2: IVehicule2): Observable<EntityResponseType> {
    return this.http.put<IVehicule2>(`${this.resourceUrl}/${this.getVehicule2Identifier(vehicule2)}`, vehicule2, { observe: 'response' });
  }

  partialUpdate(vehicule2: PartialUpdateVehicule2): Observable<EntityResponseType> {
    return this.http.patch<IVehicule2>(`${this.resourceUrl}/${this.getVehicule2Identifier(vehicule2)}`, vehicule2, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicule2>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicule2[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicule2Identifier(vehicule2: Pick<IVehicule2, 'id'>): number {
    return vehicule2.id;
  }

  compareVehicule2(o1: Pick<IVehicule2, 'id'> | null, o2: Pick<IVehicule2, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicule2Identifier(o1) === this.getVehicule2Identifier(o2) : o1 === o2;
  }

  addVehicule2ToCollectionIfMissing<Type extends Pick<IVehicule2, 'id'>>(
    vehicule2Collection: Type[],
    ...vehicule2sToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicule2s: Type[] = vehicule2sToCheck.filter(isPresent);
    if (vehicule2s.length > 0) {
      const vehicule2CollectionIdentifiers = vehicule2Collection.map(vehicule2Item => this.getVehicule2Identifier(vehicule2Item)!);
      const vehicule2sToAdd = vehicule2s.filter(vehicule2Item => {
        const vehicule2Identifier = this.getVehicule2Identifier(vehicule2Item);
        if (vehicule2CollectionIdentifiers.includes(vehicule2Identifier)) {
          return false;
        }
        vehicule2CollectionIdentifiers.push(vehicule2Identifier);
        return true;
      });
      return [...vehicule2sToAdd, ...vehicule2Collection];
    }
    return vehicule2Collection;
  }
}
