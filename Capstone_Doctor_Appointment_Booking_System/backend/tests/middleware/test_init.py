import pytest
from backend.middleware import settings, db, security, jwt_service
from backend.middleware.config import Settings
from backend.middleware.database import Database
from backend.middleware.security import Security
from backend.middleware.jwt_service import JWTService

class TestInit:
    def test_import_settings(self):
        assert settings is not None
        assert isinstance(settings, Settings)

    def test_import_db(self):
        assert db is not None
        assert isinstance(db, Database)

    def test_import_security(self):
        assert security is not None
        assert isinstance(security, Security)

    def test_import_jwt_service(self):
        assert jwt_service is not None
        assert isinstance(jwt_service, JWTService)

    def test_all_imports_work(self):
        from backend.middleware import (
            settings as s,
            db as d,
            security as sec,
            jwt_service as jwt
        )
        assert s is not None
        assert d is not None
        assert sec is not None
        assert jwt is not None
