import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vehicule-2.test-samples';

import { Vehicule2FormService } from './vehicule-2-form.service';

describe('Vehicule2 Form Service', () => {
  let service: Vehicule2FormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Vehicule2FormService);
  });

  describe('Service methods', () => {
    describe('createVehicule2FormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicule2FormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            prix: expect.any(Object),
            nbChevaux: expect.any(Object),
            marque: expect.any(Object),
            parking2: expect.any(Object),
          }),
        );
      });

      it('passing IVehicule2 should create a new form with FormGroup', () => {
        const formGroup = service.createVehicule2FormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            prix: expect.any(Object),
            nbChevaux: expect.any(Object),
            marque: expect.any(Object),
            parking2: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicule2', () => {
      it('should return NewVehicule2 for default Vehicule2 initial value', () => {
        const formGroup = service.createVehicule2FormGroup(sampleWithNewData);

        const vehicule2 = service.getVehicule2(formGroup) as any;

        expect(vehicule2).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicule2 for empty Vehicule2 initial value', () => {
        const formGroup = service.createVehicule2FormGroup();

        const vehicule2 = service.getVehicule2(formGroup) as any;

        expect(vehicule2).toMatchObject({});
      });

      it('should return IVehicule2', () => {
        const formGroup = service.createVehicule2FormGroup(sampleWithRequiredData);

        const vehicule2 = service.getVehicule2(formGroup) as any;

        expect(vehicule2).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicule2 should not enable id FormControl', () => {
        const formGroup = service.createVehicule2FormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicule2 should disable id FormControl', () => {
        const formGroup = service.createVehicule2FormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
