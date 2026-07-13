import pytest
from unittest.mock import AsyncMock, MagicMock, patch
from backend.middleware.database import Database, db
import logging

class TestDatabase:
    @pytest.mark.asyncio
    async def test_database_connect_success(self, mocker):
        mock_client = AsyncMock()
        mock_db = MagicMock()
        mock_client.admin.command = AsyncMock(return_value={"ok": 1})
        mock_client.__getitem__.return_value = mock_db

        mocker.patch('backend.middleware.database.AsyncIOMotorClient', return_value=mock_client)
        mocker.patch('backend.middleware.database.settings.MONGODB_URL', 'mongodb://test:27017')
        mocker.patch('backend.middleware.database.settings.DATABASE_NAME', 'test_db')
        mocker.patch('backend.middleware.database.Database._create_indexes', AsyncMock())

        result = await Database.connect()

        assert result == mock_db
        assert Database.client == mock_client
        assert Database.db == mock_db

    @pytest.mark.asyncio
    async def test_database_connect_failure(self, mocker):
        mocker.patch('backend.middleware.database.AsyncIOMotorClient', side_effect=Exception("Connection failed"))

        with pytest.raises(Exception) as exc_info:
            await Database.connect()

        assert "Connection failed" in str(exc_info.value)

    @pytest.mark.asyncio
    async def test_database_disconnect(self, mocker):
        mock_client = AsyncMock()
        Database.client = mock_client
        Database.db = MagicMock()

        await Database.disconnect()

        mock_client.close.assert_called_once()

    @pytest.mark.asyncio
    async def test_database_disconnect_no_client(self, mocker):
        Database.client = None
        Database.db = None

        await Database.disconnect()


    @pytest.mark.asyncio
    async def test_create_indexes_success(self, mocker):
        mock_db = MagicMock()
        mock_db.users.create_index = AsyncMock()
        Database.db = mock_db

        await Database._create_indexes()

        assert mock_db.users.create_index.call_count == 3
        mock_db.users.create_index.assert_any_call("email", unique=True)
        mock_db.users.create_index.assert_any_call("role")
        mock_db.users.create_index.assert_any_call("is_active")

    @pytest.mark.asyncio
    async def test_create_indexes_failure(self, mocker):
        mock_db = MagicMock()
        mock_db.users.create_index = AsyncMock(side_effect=Exception("Index creation failed"))
        Database.db = mock_db

        await Database._create_indexes()


    def test_get_db_success(self):
        mock_db = MagicMock()
        Database.db = mock_db

        result = Database.get_db()

        assert result == mock_db

    def test_get_db_not_initialized(self):
        Database.db = None

        with pytest.raises(ValueError) as exc_info:
            Database.get_db()

        assert "Database not initialized" in str(exc_info.value)

    def test_db_instance(self):
        assert isinstance(db, Database)
