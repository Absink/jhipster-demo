import dayjs from 'dayjs/esm';

import { IParking, NewParking } from './parking.model';

export const sampleWithRequiredData: IParking = {
  id: 7478,
};

export const sampleWithPartialData: IParking = {
  id: 20526,
  nom: 'consequently unto boohoo',
  is_open: true,
};

export const sampleWithFullData: IParking = {
  id: 18150,
  nom: 'into',
  nb_places: 26585,
  date_creation: dayjs('2023-11-29'),
  is_open: true,
};

export const sampleWithNewData: NewParking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
