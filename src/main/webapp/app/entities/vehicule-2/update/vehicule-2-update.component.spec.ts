import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IParking2 } from 'app/entities/parking-2/parking-2.model';
import { Parking2Service } from 'app/entities/parking-2/service/parking-2.service';
import { Vehicule2Service } from '../service/vehicule-2.service';
import { IVehicule2 } from '../vehicule-2.model';
import { Vehicule2FormService } from './vehicule-2-form.service';

import { Vehicule2UpdateComponent } from './vehicule-2-update.component';

describe('Vehicule2 Management Update Component', () => {
  let comp: Vehicule2UpdateComponent;
  let fixture: ComponentFixture<Vehicule2UpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehicule2FormService: Vehicule2FormService;
  let vehicule2Service: Vehicule2Service;
  let parking2Service: Parking2Service;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), Vehicule2UpdateComponent],
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
      .overrideTemplate(Vehicule2UpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(Vehicule2UpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehicule2FormService = TestBed.inject(Vehicule2FormService);
    vehicule2Service = TestBed.inject(Vehicule2Service);
    parking2Service = TestBed.inject(Parking2Service);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Parking2 query and add missing value', () => {
      const vehicule2: IVehicule2 = { id: 456 };
      const parking2: IParking2 = { id: 17819 };
      vehicule2.parking2 = parking2;

      const parking2Collection: IParking2[] = [{ id: 28986 }];
      jest.spyOn(parking2Service, 'query').mockReturnValue(of(new HttpResponse({ body: parking2Collection })));
      const additionalParking2s = [parking2];
      const expectedCollection: IParking2[] = [...additionalParking2s, ...parking2Collection];
      jest.spyOn(parking2Service, 'addParking2ToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicule2 });
      comp.ngOnInit();

      expect(parking2Service.query).toHaveBeenCalled();
      expect(parking2Service.addParking2ToCollectionIfMissing).toHaveBeenCalledWith(
        parking2Collection,
        ...additionalParking2s.map(expect.objectContaining),
      );
      expect(comp.parking2sSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vehicule2: IVehicule2 = { id: 456 };
      const parking2: IParking2 = { id: 32463 };
      vehicule2.parking2 = parking2;

      activatedRoute.data = of({ vehicule2 });
      comp.ngOnInit();

      expect(comp.parking2sSharedCollection).toContain(parking2);
      expect(comp.vehicule2).toEqual(vehicule2);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicule2>>();
      const vehicule2 = { id: 123 };
      jest.spyOn(vehicule2FormService, 'getVehicule2').mockReturnValue(vehicule2);
      jest.spyOn(vehicule2Service, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicule2 });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicule2 }));
      saveSubject.complete();

      // THEN
      expect(vehicule2FormService.getVehicule2).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehicule2Service.update).toHaveBeenCalledWith(expect.objectContaining(vehicule2));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicule2>>();
      const vehicule2 = { id: 123 };
      jest.spyOn(vehicule2FormService, 'getVehicule2').mockReturnValue({ id: null });
      jest.spyOn(vehicule2Service, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicule2: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicule2 }));
      saveSubject.complete();

      // THEN
      expect(vehicule2FormService.getVehicule2).toHaveBeenCalled();
      expect(vehicule2Service.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicule2>>();
      const vehicule2 = { id: 123 };
      jest.spyOn(vehicule2Service, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicule2 });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehicule2Service.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParking2', () => {
      it('Should forward to parking2Service', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(parking2Service, 'compareParking2');
        comp.compareParking2(entity, entity2);
        expect(parking2Service.compareParking2).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
