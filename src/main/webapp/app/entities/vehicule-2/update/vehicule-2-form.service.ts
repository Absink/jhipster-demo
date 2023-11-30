import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVehicule2, NewVehicule2 } from '../vehicule-2.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicule2 for edit and NewVehicule2FormGroupInput for create.
 */
type Vehicule2FormGroupInput = IVehicule2 | PartialWithRequiredKeyOf<NewVehicule2>;

type Vehicule2FormDefaults = Pick<NewVehicule2, 'id'>;

type Vehicule2FormGroupContent = {
  id: FormControl<IVehicule2['id'] | NewVehicule2['id']>;
  nom: FormControl<IVehicule2['nom']>;
  prix: FormControl<IVehicule2['prix']>;
  nbChevaux: FormControl<IVehicule2['nbChevaux']>;
  marque: FormControl<IVehicule2['marque']>;
  parking2: FormControl<IVehicule2['parking2']>;
};

export type Vehicule2FormGroup = FormGroup<Vehicule2FormGroupContent>;

@Injectable({ providedIn: 'root' })
export class Vehicule2FormService {
  createVehicule2FormGroup(vehicule2: Vehicule2FormGroupInput = { id: null }): Vehicule2FormGroup {
    const vehicule2RawValue = {
      ...this.getFormDefaults(),
      ...vehicule2,
    };
    return new FormGroup<Vehicule2FormGroupContent>({
      id: new FormControl(
        { value: vehicule2RawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(vehicule2RawValue.nom),
      prix: new FormControl(vehicule2RawValue.prix),
      nbChevaux: new FormControl(vehicule2RawValue.nbChevaux),
      marque: new FormControl(vehicule2RawValue.marque),
      parking2: new FormControl(vehicule2RawValue.parking2),
    });
  }

  getVehicule2(form: Vehicule2FormGroup): IVehicule2 | NewVehicule2 {
    return form.getRawValue() as IVehicule2 | NewVehicule2;
  }

  resetForm(form: Vehicule2FormGroup, vehicule2: Vehicule2FormGroupInput): void {
    const vehicule2RawValue = { ...this.getFormDefaults(), ...vehicule2 };
    form.reset(
      {
        ...vehicule2RawValue,
        id: { value: vehicule2RawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): Vehicule2FormDefaults {
    return {
      id: null,
    };
  }
}
