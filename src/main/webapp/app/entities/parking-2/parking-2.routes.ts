import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { Parking2Component } from './list/parking-2.component';
import { Parking2DetailComponent } from './detail/parking-2-detail.component';
import { Parking2UpdateComponent } from './update/parking-2-update.component';
import Parking2Resolve from './route/parking-2-routing-resolve.service';

const parking2Route: Routes = [
  {
    path: '',
    component: Parking2Component,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Parking2DetailComponent,
    resolve: {
      parking2: Parking2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Parking2UpdateComponent,
    resolve: {
      parking2: Parking2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Parking2UpdateComponent,
    resolve: {
      parking2: Parking2Resolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parking2Route;
