jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Parking2Service } from '../service/parking-2.service';

import { Parking2DeleteDialogComponent } from './parking-2-delete-dialog.component';

describe('Parking2 Management Delete Component', () => {
  let comp: Parking2DeleteDialogComponent;
  let fixture: ComponentFixture<Parking2DeleteDialogComponent>;
  let service: Parking2Service;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, Parking2DeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(Parking2DeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(Parking2DeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(Parking2Service);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
