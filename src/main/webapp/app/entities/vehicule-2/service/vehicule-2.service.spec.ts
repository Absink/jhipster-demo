import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVehicule2 } from '../vehicule-2.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../vehicule-2.test-samples';

import { Vehicule2Service } from './vehicule-2.service';

const requireRestSample: IVehicule2 = {
  ...sampleWithRequiredData,
};

describe('Vehicule2 Service', () => {
  let service: Vehicule2Service;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicule2 | IVehicule2[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(Vehicule2Service);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Vehicule2', () => {
      const vehicule2 = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicule2).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vehicule2', () => {
      const vehicule2 = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicule2).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vehicule2', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vehicule2', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vehicule2', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicule2ToCollectionIfMissing', () => {
      it('should add a Vehicule2 to an empty array', () => {
        const vehicule2: IVehicule2 = sampleWithRequiredData;
        expectedResult = service.addVehicule2ToCollectionIfMissing([], vehicule2);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicule2);
      });

      it('should not add a Vehicule2 to an array that contains it', () => {
        const vehicule2: IVehicule2 = sampleWithRequiredData;
        const vehicule2Collection: IVehicule2[] = [
          {
            ...vehicule2,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicule2ToCollectionIfMissing(vehicule2Collection, vehicule2);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vehicule2 to an array that doesn't contain it", () => {
        const vehicule2: IVehicule2 = sampleWithRequiredData;
        const vehicule2Collection: IVehicule2[] = [sampleWithPartialData];
        expectedResult = service.addVehicule2ToCollectionIfMissing(vehicule2Collection, vehicule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicule2);
      });

      it('should add only unique Vehicule2 to an array', () => {
        const vehicule2Array: IVehicule2[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicule2Collection: IVehicule2[] = [sampleWithRequiredData];
        expectedResult = service.addVehicule2ToCollectionIfMissing(vehicule2Collection, ...vehicule2Array);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicule2: IVehicule2 = sampleWithRequiredData;
        const vehicule22: IVehicule2 = sampleWithPartialData;
        expectedResult = service.addVehicule2ToCollectionIfMissing([], vehicule2, vehicule22);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicule2);
        expect(expectedResult).toContain(vehicule22);
      });

      it('should accept null and undefined values', () => {
        const vehicule2: IVehicule2 = sampleWithRequiredData;
        expectedResult = service.addVehicule2ToCollectionIfMissing([], null, vehicule2, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicule2);
      });

      it('should return initial array if no Vehicule2 is added', () => {
        const vehicule2Collection: IVehicule2[] = [sampleWithRequiredData];
        expectedResult = service.addVehicule2ToCollectionIfMissing(vehicule2Collection, undefined, null);
        expect(expectedResult).toEqual(vehicule2Collection);
      });
    });

    describe('compareVehicule2', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicule2(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVehicule2(entity1, entity2);
        const compareResult2 = service.compareVehicule2(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVehicule2(entity1, entity2);
        const compareResult2 = service.compareVehicule2(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVehicule2(entity1, entity2);
        const compareResult2 = service.compareVehicule2(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
