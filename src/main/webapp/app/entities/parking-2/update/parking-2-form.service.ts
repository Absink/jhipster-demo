import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParking2, NewParking2 } from '../parking-2.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParking2 for edit and NewParking2FormGroupInput for create.
 */
type Parking2FormGroupInput = IParking2 | PartialWithRequiredKeyOf<NewParking2>;

type Parking2FormDefaults = Pick<NewParking2, 'id' | 'isOpen'>;

type Parking2FormGroupContent = {
  id: FormControl<IParking2['id'] | NewParking2['id']>;
  nom: FormControl<IParking2['nom']>;
  nbPlaces: FormControl<IParking2['nbPlaces']>;
  dateCreation: FormControl<IParking2['dateCreation']>;
  isOpen: FormControl<IParking2['isOpen']>;
};

export type Parking2FormGroup = FormGroup<Parking2FormGroupContent>;

@Injectable({ providedIn: 'root' })
export class Parking2FormService {
  createParking2FormGroup(parking2: Parking2FormGroupInput = { id: null }): Parking2FormGroup {
    const parking2RawValue = {
      ...this.getFormDefaults(),
      ...parking2,
    };
    return new FormGroup<Parking2FormGroupContent>({
      id: new FormControl(
        { value: parking2RawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(parking2RawValue.nom),
      nbPlaces: new FormControl(parking2RawValue.nbPlaces),
      dateCreation: new FormControl(parking2RawValue.dateCreation),
      isOpen: new FormControl(parking2RawValue.isOpen),
    });
  }

  getParking2(form: Parking2FormGroup): IParking2 | NewParking2 {
    return form.getRawValue() as IParking2 | NewParking2;
  }

  resetForm(form: Parking2FormGroup, parking2: Parking2FormGroupInput): void {
    const parking2RawValue = { ...this.getFormDefaults(), ...parking2 };
    form.reset(
      {
        ...parking2RawValue,
        id: { value: parking2RawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): Parking2FormDefaults {
    return {
      id: null,
      isOpen: false,
    };
  }
}
