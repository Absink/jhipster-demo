import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ParkingComponent } from './list/parking.component';
import { ParkingDetailComponent } from './detail/parking-detail.component';
import { ParkingUpdateComponent } from './update/parking-update.component';
import ParkingResolve from './route/parking-routing-resolve.service';

const parkingRoute: Routes = [
  {
    path: '',
    component: ParkingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParkingDetailComponent,
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParkingUpdateComponent,
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParkingUpdateComponent,
    resolve: {
      parking: ParkingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parkingRoute;
