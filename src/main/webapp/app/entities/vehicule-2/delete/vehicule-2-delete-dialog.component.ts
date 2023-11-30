import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicule2 } from '../vehicule-2.model';
import { Vehicule2Service } from '../service/vehicule-2.service';

@Component({
  standalone: true,
  templateUrl: './vehicule-2-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class Vehicule2DeleteDialogComponent {
  vehicule2?: IVehicule2;

  constructor(
    protected vehicule2Service: Vehicule2Service,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicule2Service.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
