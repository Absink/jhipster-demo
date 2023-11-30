import { IVehicule2, NewVehicule2 } from './vehicule-2.model';

export const sampleWithRequiredData: IVehicule2 = {
  id: 11901,
};

export const sampleWithPartialData: IVehicule2 = {
  id: 19065,
  prix: 14938,
  nbChevaux: 20694,
};

export const sampleWithFullData: IVehicule2 = {
  id: 3291,
  nom: 'even only trigonometry',
  prix: 25711,
  nbChevaux: 31379,
  marque: 'RENAULT',
};

export const sampleWithNewData: NewVehicule2 = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
