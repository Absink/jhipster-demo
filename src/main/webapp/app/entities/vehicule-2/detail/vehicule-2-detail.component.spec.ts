import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { Vehicule2DetailComponent } from './vehicule-2-detail.component';

describe('Vehicule2 Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Vehicule2DetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: Vehicule2DetailComponent,
              resolve: { vehicule2: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(Vehicule2DetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load vehicule2 on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', Vehicule2DetailComponent);

      // THEN
      expect(instance.vehicule2).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
