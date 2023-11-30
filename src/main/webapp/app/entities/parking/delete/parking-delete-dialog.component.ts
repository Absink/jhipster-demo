import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IParking } from '../parking.model';
import { ParkingService } from '../service/parking.service';

@Component({
  standalone: true,
  templateUrl: './parking-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ParkingDeleteDialogComponent {
  parking?: IParking;

  constructor(
    protected parkingService: ParkingService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parkingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
