import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParkingService } from '../service/parking.service';
import { IParking } from '../parking.model';
import { ParkingFormService } from './parking-form.service';

import { ParkingUpdateComponent } from './parking-update.component';

describe('Parking Management Update Component', () => {
  let comp: ParkingUpdateComponent;
  let fixture: ComponentFixture<ParkingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parkingFormService: ParkingFormService;
  let parkingService: ParkingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ParkingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ParkingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParkingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parkingFormService = TestBed.inject(ParkingFormService);
    parkingService = TestBed.inject(ParkingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const parking: IParking = { id: 456 };

      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      expect(comp.parking).toEqual(parking);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 123 };
      jest.spyOn(parkingFormService, 'getParking').mockReturnValue(parking);
      jest.spyOn(parkingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking }));
      saveSubject.complete();

      // THEN
      expect(parkingFormService.getParking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parkingService.update).toHaveBeenCalledWith(expect.objectContaining(parking));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 123 };
      jest.spyOn(parkingFormService, 'getParking').mockReturnValue({ id: null });
      jest.spyOn(parkingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking }));
      saveSubject.complete();

      // THEN
      expect(parkingFormService.getParking).toHaveBeenCalled();
      expect(parkingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking>>();
      const parking = { id: 123 };
      jest.spyOn(parkingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parkingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
