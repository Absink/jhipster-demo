import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParking2 } from '../parking-2.model';
import { Parking2Service } from '../service/parking-2.service';

export const parking2Resolve = (route: ActivatedRouteSnapshot): Observable<null | IParking2> => {
  const id = route.params['id'];
  if (id) {
    return inject(Parking2Service)
      .find(id)
      .pipe(
        mergeMap((parking2: HttpResponse<IParking2>) => {
          if (parking2.body) {
            return of(parking2.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default parking2Resolve;
