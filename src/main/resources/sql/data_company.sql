INSERT INTO company_companies (id, created_at, created_by, code, name, active)
SELECT
    '49467a99-801e-459d-b82a-5315e874cb29', now(), 'eaf4c2f0-86ef-11ea-bc55-0242ac130003', 'C0001', 'GELODIA','1'
WHERE NOT EXISTS (
        SELECT * FROM company_companies WHERE  id = '49467a99-801e-459d-b82a-5315e874cb29'
    );