import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParkingDetailComponent } from './parking-detail.component';

describe('Parking Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParkingDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ParkingDetailComponent,
              resolve: { parking: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ParkingDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load parking on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ParkingDetailComponent);

      // THEN
      expect(instance.parking).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
