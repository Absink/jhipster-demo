import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IParking2 } from '../parking-2.model';
import { Parking2Service } from '../service/parking-2.service';
import { Parking2FormService, Parking2FormGroup } from './parking-2-form.service';

@Component({
  standalone: true,
  selector: 'jhi-parking-2-update',
  templateUrl: './parking-2-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class Parking2UpdateComponent implements OnInit {
  isSaving = false;
  parking2: IParking2 | null = null;

  editForm: Parking2FormGroup = this.parking2FormService.createParking2FormGroup();

  constructor(
    protected parking2Service: Parking2Service,
    protected parking2FormService: Parking2FormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parking2 }) => {
      this.parking2 = parking2;
      if (parking2) {
        this.updateForm(parking2);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parking2 = this.parking2FormService.getParking2(this.editForm);
    if (parking2.id !== null) {
      this.subscribeToSaveResponse(this.parking2Service.update(parking2));
    } else {
      this.subscribeToSaveResponse(this.parking2Service.create(parking2));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParking2>>): void {
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

  protected updateForm(parking2: IParking2): void {
    this.parking2 = parking2;
    this.parking2FormService.resetForm(this.editForm, parking2);
  }
}
