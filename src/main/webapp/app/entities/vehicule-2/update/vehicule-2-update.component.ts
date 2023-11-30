import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParking2 } from 'app/entities/parking-2/parking-2.model';
import { Parking2Service } from 'app/entities/parking-2/service/parking-2.service';
import { Marque } from 'app/entities/enumerations/marque.model';
import { Vehicule2Service } from '../service/vehicule-2.service';
import { IVehicule2 } from '../vehicule-2.model';
import { Vehicule2FormService, Vehicule2FormGroup } from './vehicule-2-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicule-2-update',
  templateUrl: './vehicule-2-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class Vehicule2UpdateComponent implements OnInit {
  isSaving = false;
  vehicule2: IVehicule2 | null = null;
  marqueValues = Object.keys(Marque);

  parking2sSharedCollection: IParking2[] = [];

  editForm: Vehicule2FormGroup = this.vehicule2FormService.createVehicule2FormGroup();

  constructor(
    protected vehicule2Service: Vehicule2Service,
    protected vehicule2FormService: Vehicule2FormService,
    protected parking2Service: Parking2Service,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareParking2 = (o1: IParking2 | null, o2: IParking2 | null): boolean => this.parking2Service.compareParking2(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicule2 }) => {
      this.vehicule2 = vehicule2;
      if (vehicule2) {
        this.updateForm(vehicule2);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicule2 = this.vehicule2FormService.getVehicule2(this.editForm);
    if (vehicule2.id !== null) {
      this.subscribeToSaveResponse(this.vehicule2Service.update(vehicule2));
    } else {
      this.subscribeToSaveResponse(this.vehicule2Service.create(vehicule2));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicule2>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehicule2: IVehicule2): void {
    this.vehicule2 = vehicule2;
    this.vehicule2FormService.resetForm(this.editForm, vehicule2);

    this.parking2sSharedCollection = this.parking2Service.addParking2ToCollectionIfMissing<IParking2>(
      this.parking2sSharedCollection,
      vehicule2.parking2,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.parking2Service
      .query()
      .pipe(map((res: HttpResponse<IParking2[]>) => res.body ?? []))
      .pipe(
        map((parking2s: IParking2[]) =>
          this.parking2Service.addParking2ToCollectionIfMissing<IParking2>(parking2s, this.vehicule2?.parking2),
        ),
      )
      .subscribe((parking2s: IParking2[]) => (this.parking2sSharedCollection = parking2s));
  }
}
