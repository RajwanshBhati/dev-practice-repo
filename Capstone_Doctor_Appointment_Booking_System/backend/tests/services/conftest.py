import pytest
from unittest.mock import AsyncMock, patch

@pytest.fixture(autouse=True)
def mock_db_connection():
    """Mock database connection for all service tests."""
    with patch('backend.middleware.database.db.get_db') as mock_get_db:
        mock_db = AsyncMock()
        mock_get_db.return_value = mock_db
        yield mock_db
