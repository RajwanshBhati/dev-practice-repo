import React, { useState, useRef } from 'react';
import { Button, Modal } from 'react-bootstrap';
import { FaCamera, FaUserMd, FaUpload } from 'react-icons/fa';
import { updateProfilePicture } from '../../api/doctor';
import toast from 'react-hot-toast';

const MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];

const ProfilePicture = ({ profilePicture, fullName, onUpdate }) => {
  const [showModal, setShowModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);

  const handleFileChange = (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if (!ALLOWED_TYPES.includes(file.type)) {
      toast.error('Only JPEG, PNG, WEBP or GIF images are allowed');
      return;
    }
    if (file.size > MAX_SIZE_BYTES) {
      toast.error('Image must be smaller than 5MB');
      return;
    }

    setSelectedFile(file);
    setPreviewUrl(URL.createObjectURL(file));
  };

  const handleSave = async () => {
    if (!selectedFile) {
      toast.error('Please choose a photo to upload');
      return;
    }

    setLoading(true);
    try {
      const result = await updateProfilePicture(selectedFile);
      onUpdate(result.doctor?.profile_picture || previewUrl);
      setShowModal(false);
      setSelectedFile(null);
      toast.success('Profile picture updated');
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedFile(null);
    setPreviewUrl('');
  };

  return (
    <>
      <div className="position-relative d-inline-block">
        <div
          className="rounded-circle d-flex align-items-center justify-content-center mx-auto"
          style={{
            width: '120px',
            height: '120px',
            fontSize: '48px',
            color: 'white',
            background: profilePicture ? 'none' : 'linear-gradient(135deg, #4a90d9, #357abd)',
            backgroundImage: profilePicture ? `url(${profilePicture})` : 'none',
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            border: '4px solid white',
            boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
          }}
        >
          {!profilePicture && (fullName?.charAt(0) || <FaUserMd />)}
        </div>
        <Button
          variant="primary"
          size="sm"
          className="position-absolute bottom-0 end-0 rounded-circle"
          style={{ width: '36px', height: '36px', padding: '0' }}
          onClick={() => setShowModal(true)}
        >
          <FaCamera />
        </Button>
      </div>

      <Modal show={showModal} onHide={closeModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Update Profile Picture</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="text-center mb-3">
            <img
              src={previewUrl || profilePicture || 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="100" height="100"%3E%3C/svg%3E'}
              alt="Preview"
              style={{
                width: '120px',
                height: '120px',
                borderRadius: '50%',
                objectFit: 'cover',
                border: '2px solid #e2e8f0',
                background: '#f0f4f8',
              }}
            />
          </div>

          <input
            type="file"
            accept="image/jpeg,image/png,image/webp,image/gif"
            ref={fileInputRef}
            onChange={handleFileChange}
            style={{ display: 'none' }}
          />
          <Button
            variant="outline-primary"
            className="w-100"
            onClick={() => fileInputRef.current?.click()}
            style={{ borderRadius: '8px' }}
          >
            <FaUpload className="me-2" /> Choose Photo
          </Button>
          {selectedFile && (
            <p className="text-muted mt-2 mb-0 text-center" style={{ fontSize: '0.85rem' }}>
              {selectedFile.name}
            </p>
          )}
          <p className="text-muted mt-2 mb-0" style={{ fontSize: '0.8rem' }}>
            JPEG, PNG, WEBP or GIF. Max 5MB.
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closeModal}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSave} disabled={loading || !selectedFile}>
            {loading ? 'Uploading...' : 'Save'}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProfilePicture;
