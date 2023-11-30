import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParking } from '../parking.model';
import { ParkingService } from '../service/parking.service';
import { ParkingFormService, ParkingFormGroup } from './parking-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parking-update',
  templateUrl: './parking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParkingUpdateComponent implements OnInit {
  isSaving = false;
  parking: IParking | null = null;

  editForm: ParkingFormGroup = this.parkingFormService.createParkingFormGroup();

  constructor(
    protected parkingService: ParkingService,
    protected parkingFormService: ParkingFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parking }) => {
      this.parking = parking;
      if (parking) {
        this.updateForm(parking);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parking = this.parkingFormService.getParking(this.editForm);
    if (parking.id !== null) {
      this.subscribeToSaveResponse(this.parkingService.update(parking));
    } else {
      this.subscribeToSaveResponse(this.parkingService.create(parking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParking>>): void {
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

  protected updateForm(parking: IParking): void {
    this.parking = parking;
    this.parkingFormService.resetForm(this.editForm, parking);
  }
}
