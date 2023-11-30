import { IParking2 } from 'app/entities/parking-2/parking-2.model';
import { Marque } from 'app/entities/enumerations/marque.model';

export interface IVehicule2 {
  id: number;
  nom?: string | null;
  prix?: number | null;
  nbChevaux?: number | null;
  marque?: keyof typeof Marque | null;
  parking2?: Pick<IParking2, 'id'> | null;
}

export type NewVehicule2 = Omit<IVehicule2, 'id'> & { id: null };
