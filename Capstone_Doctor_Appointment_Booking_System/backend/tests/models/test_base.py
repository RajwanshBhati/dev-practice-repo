import pytest
from datetime import datetime
from backend.models.base import BaseDBModel

class TestModel(BaseDBModel):
    name: str

class TestBaseDBModel:
    def test_base_model_creation(self):
        model = TestModel(name="test")
        assert model.id is None
        assert isinstance(model.created_at, datetime)
        assert isinstance(model.updated_at, datetime)

    def test_base_model_with_id(self):
        model = TestModel(name="test", id="test_id")
        assert model.id == "test_id"

    def test_base_model_id_conversion(self, mocker):
        mock_object_id = mocker.Mock()
        mock_object_id.__str__ = mocker.Mock(return_value="mocked_id")
        model = TestModel(name="test", id=mock_object_id)
        assert model.id == "mocked_id"

    def test_base_model_config(self):
        config = TestModel.Config
        assert config.populate_by_name is True
        assert config.arbitrary_types_allowed is True
        assert hasattr(config, 'json_encoders')
