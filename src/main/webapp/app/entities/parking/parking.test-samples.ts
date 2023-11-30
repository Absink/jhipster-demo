import dayjs from 'dayjs/esm';

import { IParking, NewParking } from './parking.model';

export const sampleWithRequiredData: IParking = {
  id: 26662,
};

export const sampleWithPartialData: IParking = {
  id: 14211,
  nom: 'smart curtain',
};

export const sampleWithFullData: IParking = {
  id: 21170,
  nom: 'supposing',
  nb_places: 7781,
  date_creation: dayjs('2023-11-30'),
  is_open: true,
};

export const sampleWithNewData: NewParking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
