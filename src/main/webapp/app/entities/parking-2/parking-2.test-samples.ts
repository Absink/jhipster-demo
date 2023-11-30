import dayjs from 'dayjs/esm';

import { IParking2, NewParking2 } from './parking-2.model';

export const sampleWithRequiredData: IParking2 = {
  id: 31078,
};

export const sampleWithPartialData: IParking2 = {
  id: 14939,
  nom: 'amongst across',
  dateCreation: dayjs('2023-11-30'),
  isOpen: false,
};

export const sampleWithFullData: IParking2 = {
  id: 13113,
  nom: 'liberalise sinful',
  nbPlaces: 23356,
  dateCreation: dayjs('2023-11-29'),
  isOpen: true,
};

export const sampleWithNewData: NewParking2 = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
