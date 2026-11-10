-- CHI DANH CHO LAB TREN MAY CA NHAN. Chay voi quyen quan tri SQL Server.
-- Mat khau lab: <DAT_MAT_KHAU_SQL_CUA_BAN> - chi dung tren may ca nhan; neu doi thi go dung mat khau moi tren form ket noi.
USE [master];
GO
IF NOT EXISTS (SELECT 1 FROM sys.sql_logins WHERE name = N'lab1_user')
BEGIN
    CREATE LOGIN [lab1_user]
        WITH PASSWORD = '<DAT_MAT_KHAU_SQL_CUA_BAN>',
        CHECK_POLICY = ON,
        CHECK_EXPIRATION = OFF;
END;
GO
USE [LAB1_MaHoaCoDien];
GO
IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'lab1_user')
    CREATE USER [lab1_user] FOR LOGIN [lab1_user];
GO
-- Cap quyen vua du: SELECT, INSERT, DELETE lich su. Khong cap db_owner.
GRANT SELECT, INSERT, DELETE ON OBJECT::dbo.history TO [lab1_user];
GO
