import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParking, NewParking } from '../parking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParking for edit and NewParkingFormGroupInput for create.
 */
type ParkingFormGroupInput = IParking | PartialWithRequiredKeyOf<NewParking>;

type ParkingFormDefaults = Pick<NewParking, 'id' | 'is_open'>;

type ParkingFormGroupContent = {
  id: FormControl<IParking['id'] | NewParking['id']>;
  nom: FormControl<IParking['nom']>;
  nb_places: FormControl<IParking['nb_places']>;
  date_creation: FormControl<IParking['date_creation']>;
  is_open: FormControl<IParking['is_open']>;
};

export type ParkingFormGroup = FormGroup<ParkingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParkingFormService {
  createParkingFormGroup(parking: ParkingFormGroupInput = { id: null }): ParkingFormGroup {
    const parkingRawValue = {
      ...this.getFormDefaults(),
      ...parking,
    };
    return new FormGroup<ParkingFormGroupContent>({
      id: new FormControl(
        { value: parkingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(parkingRawValue.nom),
      nb_places: new FormControl(parkingRawValue.nb_places),
      date_creation: new FormControl(parkingRawValue.date_creation),
      is_open: new FormControl(parkingRawValue.is_open),
    });
  }

  getParking(form: ParkingFormGroup): IParking | NewParking {
    return form.getRawValue() as IParking | NewParking;
  }

  resetForm(form: ParkingFormGroup, parking: ParkingFormGroupInput): void {
    const parkingRawValue = { ...this.getFormDefaults(), ...parking };
    form.reset(
      {
        ...parkingRawValue,
        id: { value: parkingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParkingFormDefaults {
    return {
      id: null,
      is_open: false,
    };
  }
}
