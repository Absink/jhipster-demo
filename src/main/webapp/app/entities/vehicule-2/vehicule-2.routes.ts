import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { Vehicule2Component } from './list/vehicule-2.component';
import { Vehicule2DetailComponent } from './detail/vehicule-2-detail.component';
import { Vehicule2UpdateComponent } from './update/vehicule-2-update.component';
import Vehicule2Resolve from './route/vehicule-2-routing-resolve.service';

const vehicule2Route: Routes = [
  {
    path: '',
    component: Vehicule2Component,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Vehicule2DetailComponent,
    resolve: {
      vehicule2: Vehicule2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Vehicule2UpdateComponent,
    resolve: {
      vehicule2: Vehicule2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Vehicule2UpdateComponent,
    resolve: {
      vehicule2: Vehicule2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicule2Route;
