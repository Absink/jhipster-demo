import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { Parking2DetailComponent } from './parking-2-detail.component';

describe('Parking2 Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Parking2DetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: Parking2DetailComponent,
              resolve: { parking2: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(Parking2DetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load parking2 on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', Parking2DetailComponent);

      // THEN
      expect(instance.parking2).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
