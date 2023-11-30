import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'parking',
        data: { pageTitle: 'demoApp.parking.home.title' },
        loadChildren: () => import('./parking/parking.routes'),
      },
      {
        path: 'parking-2',
        data: { pageTitle: 'demoApp.parking2.home.title' },
        loadChildren: () => import('./parking-2/parking-2.routes'),
      },
      {
        path: 'vehicule-2',
        data: { pageTitle: 'demoApp.vehicule2.home.title' },
        loadChildren: () => import('./vehicule-2/vehicule-2.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
