import dayjs from 'dayjs/esm';

import { IParking2, NewParking2 } from './parking-2.model';

export const sampleWithRequiredData: IParking2 = {
  id: 7960,
};

export const sampleWithPartialData: IParking2 = {
  id: 22541,
  nom: 'chastity pfft till',
  isOpen: true,
};

export const sampleWithFullData: IParking2 = {
  id: 15964,
  nom: 'silly beneath',
  nbPlaces: 6765,
  dateCreation: dayjs('2023-11-30'),
  isOpen: false,
};

export const sampleWithNewData: NewParking2 = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
