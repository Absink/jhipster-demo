import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { Parking2Service } from '../service/parking-2.service';
import { IParking2 } from '../parking-2.model';
import { Parking2FormService } from './parking-2-form.service';

import { Parking2UpdateComponent } from './parking-2-update.component';

describe('Parking2 Management Update Component', () => {
  let comp: Parking2UpdateComponent;
  let fixture: ComponentFixture<Parking2UpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parking2FormService: Parking2FormService;
  let parking2Service: Parking2Service;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), Parking2UpdateComponent],
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
      .overrideTemplate(Parking2UpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(Parking2UpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parking2FormService = TestBed.inject(Parking2FormService);
    parking2Service = TestBed.inject(Parking2Service);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const parking2: IParking2 = { id: 456 };

      activatedRoute.data = of({ parking2 });
      comp.ngOnInit();

      expect(comp.parking2).toEqual(parking2);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking2>>();
      const parking2 = { id: 123 };
      jest.spyOn(parking2FormService, 'getParking2').mockReturnValue(parking2);
      jest.spyOn(parking2Service, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking2 });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking2 }));
      saveSubject.complete();

      // THEN
      expect(parking2FormService.getParking2).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parking2Service.update).toHaveBeenCalledWith(expect.objectContaining(parking2));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking2>>();
      const parking2 = { id: 123 };
      jest.spyOn(parking2FormService, 'getParking2').mockReturnValue({ id: null });
      jest.spyOn(parking2Service, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking2: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parking2 }));
      saveSubject.complete();

      // THEN
      expect(parking2FormService.getParking2).toHaveBeenCalled();
      expect(parking2Service.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParking2>>();
      const parking2 = { id: 123 };
      jest.spyOn(parking2Service, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parking2 });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parking2Service.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
