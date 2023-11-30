import dayjs from 'dayjs/esm';

export interface IParking {
  id: number;
  nom?: string | null;
  nb_places?: number | null;
  date_creation?: dayjs.Dayjs | null;
  is_open?: boolean | null;
}

export type NewParking = Omit<IParking, 'id'> & { id: null };
