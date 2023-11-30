import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../parking-2.test-samples';

import { Parking2FormService } from './parking-2-form.service';

describe('Parking2 Form Service', () => {
  let service: Parking2FormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Parking2FormService);
  });

  describe('Service methods', () => {
    describe('createParking2FormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParking2FormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            nbPlaces: expect.any(Object),
            dateCreation: expect.any(Object),
            isOpen: expect.any(Object),
          }),
        );
      });

      it('passing IParking2 should create a new form with FormGroup', () => {
        const formGroup = service.createParking2FormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            nbPlaces: expect.any(Object),
            dateCreation: expect.any(Object),
            isOpen: expect.any(Object),
          }),
        );
      });
    });

    describe('getParking2', () => {
      it('should return NewParking2 for default Parking2 initial value', () => {
        const formGroup = service.createParking2FormGroup(sampleWithNewData);

        const parking2 = service.getParking2(formGroup) as any;

        expect(parking2).toMatchObject(sampleWithNewData);
      });

      it('should return NewParking2 for empty Parking2 initial value', () => {
        const formGroup = service.createParking2FormGroup();

        const parking2 = service.getParking2(formGroup) as any;

        expect(parking2).toMatchObject({});
      });

      it('should return IParking2', () => {
        const formGroup = service.createParking2FormGroup(sampleWithRequiredData);

        const parking2 = service.getParking2(formGroup) as any;

        expect(parking2).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParking2 should not enable id FormControl', () => {
        const formGroup = service.createParking2FormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParking2 should disable id FormControl', () => {
        const formGroup = service.createParking2FormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
