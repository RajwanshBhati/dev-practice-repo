import React, { useState } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { FaCamera, FaUserMd } from 'react-icons/fa';
import { updateProfilePicture } from '../../api/doctor';
import toast from 'react-hot-toast';

const ProfilePicture = ({ profilePicture, fullName, onUpdate }) => {
  const [showModal, setShowModal] = useState(false);
  const [imageUrl, setImageUrl] = useState(profilePicture || '');
  const [loading, setLoading] = useState(false);

  /**
   * Handle save profile picture.
   */
  const handleSave = async () => {
    if (!imageUrl.trim()) {
      toast.error('Please enter a valid image URL');
      return;
    }

    setLoading(true);
    try {
      await updateProfilePicture({ profile_picture: imageUrl });
      onUpdate(imageUrl);
      setShowModal(false);
      toast.success('Profile picture updated');
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
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

      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Update Profile Picture</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label className="fw-semibold">Image URL</Form.Label>
            <Form.Control
              type="text"
              placeholder="https://example.com/image.jpg"
              value={imageUrl}
              onChange={(e) => setImageUrl(e.target.value)}
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            />
            <Form.Text className="text-muted">
              Enter a valid image URL for your profile picture
            </Form.Text>
          </Form.Group>
          {imageUrl && (
            <div className="mt-3 text-center">
              <img
                src={imageUrl}
                alt="Preview"
                style={{
                  width: '100px',
                  height: '100px',
                  borderRadius: '50%',
                  objectFit: 'cover',
                  border: '2px solid #e2e8f0',
                }}
              />
            </div>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSave} disabled={loading}>
            {loading ? 'Saving...' : 'Save'}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProfilePicture;
