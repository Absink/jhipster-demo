import dayjs from 'dayjs/esm';

export interface IParking2 {
  id: number;
  nom?: string | null;
  nbPlaces?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  isOpen?: boolean | null;
}

export type NewParking2 = Omit<IParking2, 'id'> & { id: null };
