import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IParking2 } from '../parking-2.model';
import { Parking2Service } from '../service/parking-2.service';

@Component({
  standalone: true,
  templateUrl: './parking-2-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class Parking2DeleteDialogComponent {
  parking2?: IParking2;

  constructor(
    protected parking2Service: Parking2Service,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parking2Service.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
