import React, { useState, useEffect, useCallback } from 'react';
import {
  getAvailabilitySlots,
  createAvailability,
  updateAvailabilitySlot,
  deleteAvailabilitySlot,
  getAvailabilityStats,
} from '../../api/availability';
import { Container, Row, Col, Card, Button, Badge,Form} from 'react-bootstrap';
import { FaPlus, FaEdit, FaTrash, FaClock, FaCalendarAlt } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import SlotForm from './SlotForm';
import SlotStats from './SlotStats';

const ManageAvailability = () => {
  const [loading, setLoading] = useState(true);
  const [slots, setSlots] = useState([]);
  const [stats, setStats] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingSlot, setEditingSlot] = useState(null);
  const [dateFilter, setDateFilter] = useState('');

  /**
   * Load slots and stats on mount.
   */
  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Load slots when date filter changes.
   */
  useEffect(() => {
    loadSlots();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dateFilter]);

  /**
   * Load slots and stats data.
   */
  const loadData = async () => {
    setLoading(true);
    try {
      await Promise.all([loadSlots(), loadStats()]);
    } catch (error) {
      console.error('Error loading data:', error);
      toast.error('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Load availability slots.
   */
  const loadSlots = async () => {
    try {
      const params = {};
      if (dateFilter) {
        params.date = dateFilter;
      }
      const data = await getAvailabilitySlots(params);
      setSlots(data.availabilities || []);
    } catch (error) {
      console.error('Error loading slots:', error);
      toast.error('Failed to load slots');
    }
  };

  /**
   * Load availability statistics.
   */
  const loadStats = async () => {
    try {
      const data = await getAvailabilityStats();
      setStats(data);
    } catch (error) {
      console.error('Error loading stats:', error);
      toast.error('Failed to load statistics');
    }
  };

  /**
   * Handle create slot.
   */
  const handleCreate = async (data) => {
    await createAvailability(data);
    toast.success('Slot added successfully');
    await Promise.all([loadSlots(), loadStats()]);
  };

  /**
   * Handle update slot.
   */
  const handleUpdate = async (data) => {
    await updateAvailabilitySlot(editingSlot.id, data);
    toast.success('Slot updated successfully');
    await Promise.all([loadSlots(), loadStats()]);
  };

  /**
   * Handle delete slot.
   */
  const handleDelete = async (slotId) => {
    if (!window.confirm('Are you sure you want to delete this slot?')) {
      return;
    }

    try {
      await deleteAvailabilitySlot(slotId);
      toast.success('Slot deleted successfully');
      await Promise.all([loadSlots(), loadStats()]);
    } catch (error) {
      // Error handled by axios interceptor
    }
  };

  /**
   * Open edit form.
   */
  const handleEdit = (slot) => {
    setEditingSlot(slot);
    setShowForm(true);
  };

  /**
   * Open create form.
   */
  const handleAdd = () => {
    setEditingSlot(null);
    setShowForm(true);
  };

  /**
   * Close form.
   */
  const handleFormClose = () => {
    setShowForm(false);
    setEditingSlot(null);
  };

  /**
   * Handle save from form.
   */
  const handleSave = async (data) => {
    if (editingSlot) {
      await handleUpdate(data);
    } else {
      await handleCreate(data);
    }
  };

  /**
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  if (loading) {
    return <Loading message="Loading availability..." />;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Manage Availability
        </h1>
        <Button
          variant="primary"
          onClick={handleAdd}
          style={{ borderRadius: '8px' }}
        >
          <FaPlus className="me-2" /> Add Slot
        </Button>
      </div>

      {/* Stats */}
      <SlotStats stats={stats} loading={loading} />

      {/* Date Filter */}
      <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-3">
          <div className="d-flex gap-3 align-items-center">
            <label className="fw-semibold text-secondary">Filter by Date:</label>
            <Form.Control
              type="date"
              value={dateFilter}
              onChange={(e) => setDateFilter(e.target.value)}
              style={{
                width: '200px',
                borderRadius: '8px',
                border: '2px solid #e2e8f0',
              }}
            />
            {dateFilter && (
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={() => setDateFilter('')}
                style={{ borderRadius: '8px' }}
              >
                Clear
              </Button>
            )}
          </div>
        </Card.Body>
      </Card>

      {/* Slots List */}
      {slots.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}>📅</div>
          <h4>No slots found</h4>
          <p className="text-muted">
            {dateFilter
              ? 'No slots available on this date.'
              : 'You haven\'t created any availability slots yet.'}
          </p>
          <Button variant="primary" onClick={handleAdd}>
            <FaPlus className="me-2" /> Create Your First Slot
          </Button>
        </div>
      ) : (
        <Row>
          {slots.map((slot) => (
            <Col md={6} lg={4} key={slot.id} className="mb-3">
              <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
                <Card.Body className="p-4">
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <Badge
                      bg={slot.is_available ? 'success' : 'danger'}
                      className="px-3 py-2"
                    >
                      {slot.is_available ? 'Available' : 'Booked'}
                    </Badge>
                    <div className="d-flex gap-2">
                      {slot.is_available && (
                        <>
                          <Button
                            variant="outline-primary"
                            size="sm"
                            onClick={() => handleEdit(slot)}
                            style={{ borderRadius: '8px' }}
                          >
                            <FaEdit />
                          </Button>
                          <Button
                            variant="outline-danger"
                            size="sm"
                            onClick={() => handleDelete(slot.id)}
                            style={{ borderRadius: '8px' }}
                          >
                            <FaTrash />
                          </Button>
                        </>
                      )}
                    </div>
                  </div>

                  <div className="mb-2">
                    <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                      <FaCalendarAlt className="me-1" /> Date
                    </p>
                    <p className="fw-semibold mb-0">{formatDate(slot.date)}</p>
                  </div>

                  <div className="mb-2">
                    <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                      <FaClock className="me-1" /> Time
                    </p>
                    <p className="fw-semibold mb-0">
                      {slot.start_time} - {slot.end_time}
                    </p>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}

      {/* Slot Form Modal */}
      <SlotForm
        show={showForm}
        onHide={handleFormClose}
        slot={editingSlot}
        onSave={handleSave}
      />
    </Container>
  );
};

export default ManageAvailability;
