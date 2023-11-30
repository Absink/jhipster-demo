import { IVehicule2, NewVehicule2 } from './vehicule-2.model';

export const sampleWithRequiredData: IVehicule2 = {
  id: 31099,
};

export const sampleWithPartialData: IVehicule2 = {
  id: 18482,
  prix: 26465,
  nbChevaux: 1961,
};

export const sampleWithFullData: IVehicule2 = {
  id: 85,
  nom: 'wherever',
  prix: 20378,
  nbChevaux: 31741,
  marque: 'PORSCHE',
};

export const sampleWithNewData: NewVehicule2 = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
