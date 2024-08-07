import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const Events = () => {
    const [events, setEvents] = useState([]);
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [filters, setFilters] = useState({
        category: '',
        startDate: '',
        endDate: '',
        location: '',
        minPrice: '',
        maxPrice: ''
    });

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/events');
                const eventData = response.data;

                const eventsWithImageUrls = await Promise.all(eventData.map(async (event) => {
                    try {
                        if (event.imageId) {
                            const imgResponse = await axios.get(`http://localhost:8080/api/images/${event.imageId}`, { responseType: 'arraybuffer' });
                            const imgBlob = new Blob([imgResponse.data], { type: imgResponse.headers['content-type'] });
                            const imageUrl = URL.createObjectURL(imgBlob);
                            return { ...event, imageUrl };
                        }
                        return { ...event, imageUrl: 'https://via.placeholder.com/150' };
                    } catch (imgErr) {
                        console.error('Error fetching individual image:', imgErr);
                        return { ...event, imageUrl: 'https://via.placeholder.com/150' };
                    }
                }));

                setEvents(eventsWithImageUrls);
                setFilteredEvents(eventsWithImageUrls);
                setError(null);
            } catch (err) {
                console.error('Error fetching events:', err.response ? err.response.data : err.message);
                setError('Error fetching events');
            }
        };

        fetchEvents();
    }, []);

    useEffect(() => {
        applyFiltersAndSearch();
    }, [filters, searchTerm, events]);

    const applyFiltersAndSearch = () => {
        let filtered = [...events];

        if (filters.category) {
            filtered = filtered.filter(event => event.eventCategory.toLowerCase() === filters.category.toLowerCase());
        }

        if (filters.startDate && filters.endDate) {
            filtered = filtered.filter(event =>
                new Date(event.eventDate) >= new Date(filters.startDate) &&
                new Date(event.eventDate) <= new Date(filters.endDate)
            );
        }

        if (filters.location) {
            filtered = filtered.filter(event => event.eventLocation.toLowerCase().includes(filters.location.toLowerCase()));
        }

        if (filters.minPrice && filters.maxPrice) {
            filtered = filtered.filter(event =>
                event.eventPrice >= parseFloat(filters.minPrice) &&
                event.eventPrice <= parseFloat(filters.maxPrice)
            );
        }

        if (searchTerm.trim() !== '') {
            filtered = filtered.filter(event =>
                event.eventName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                event.eventLocation.toLowerCase().includes(searchTerm.toLowerCase()) ||
                event.eventCategory.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        setFilteredEvents(filtered);
    };

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const clearFilters = () => {
        setFilters({
            category: '',
            startDate: '',
            endDate: '',
            location: '',
            minPrice: '',
            maxPrice: ''
        });
        setSearchTerm('');
    };

    const formatTime = (timeArray) => {
        if (!Array.isArray(timeArray) || timeArray.length !== 2) {
            return '';
        }
        const [hours, minutes] = timeArray;
        const time = new Date();
        time.setHours(hours);
        time.setMinutes(minutes);
        return time.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
    };

    const user = JSON.parse(localStorage.getItem('user')) || {};
    const isAdmin = user.role === 'admin';

    if (!events.length && !error) {
        return <p>Loading...</p>;
    }

    return (
        <div className='container py-5'>
            <div className='card shadow-sm'>
                <div className='card-body'>
                    {error && <div className="alert alert-danger">{error}</div>}

                    <div className='mb-3 d-flex justify-content-between align-items-center'>
                        <h1 className='text-primary'>Event List</h1>
                        <div>
                            <Link to="/create-event" className='btn btn-primary me-2'>Create Event</Link>
                            {isAdmin && <Link to="/admin-dashboard" className='btn btn-secondary me-2'>Admin Dashboard</Link>}
                            <Link to="/login" className='btn btn-outline-primary me-2'>Login</Link>
                            <Link to="/register" className='btn btn-outline-primary me-2'>Register</Link>
                            <Link to="/about" className='btn btn-outline-primary me-2'>About</Link>
                            <Link to="/contact" className='btn btn-outline-primary'>Contact</Link>
                        </div>
                    </div>

                    <input
                        type='text'
                        className='form-control mb-3'
                        placeholder='Search events...'
                        value={searchTerm}
                        onChange={handleSearch}
                    />

                    <div className='row mb-3'>
                        <div className='col-md-2'>
                            <input
                                type='text'
                                className='form-control form-control-sm'
                                placeholder='Category'
                                value={filters.category}
                                onChange={(e) => setFilters({ ...filters, category: e.target.value })}
                            />
                        </div>
                        <div className='col-md-2'>
                            <input
                                type='date'
                                className='form-control form-control-sm'
                                placeholder='Start Date'
                                value={filters.startDate}
                                onChange={(e) => setFilters({ ...filters, startDate: e.target.value })}
                            />
                        </div>
                        <div className='col-md-2'>
                            <input
                                type='date'
                                className='form-control form-control-sm'
                                placeholder='End Date'
                                value={filters.endDate}
                                onChange={(e) => setFilters({ ...filters, endDate: e.target.value })}
                            />
                        </div>
                        <div className='col-md-2'>
                            <input
                                type='text'
                                className='form-control form-control-sm'
                                placeholder='Location'
                                value={filters.location}
                                onChange={(e) => setFilters({ ...filters, location: e.target.value })}
                            />
                        </div>
                        <div className='col-md-2'>
                            <input
                                type='number'
                                className='form-control form-control-sm'
                                placeholder='Min Price'
                                value={filters.minPrice}
                                onChange={(e) => setFilters({ ...filters, minPrice: e.target.value })}
                            />
                        </div>
                        <div className='col-md-2'>
                            <input
                                type='number'
                                className='form-control form-control-sm'
                                placeholder='Max Price'
                                value={filters.maxPrice}
                                onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value })}
                            />
                        </div>
                    </div>

                    <div className='row row-cols-1 row-cols-md-2 g-4'>
                        {filteredEvents.map((event, index) => (
                            <div key={index} className='col'>
                                <div className='card'>
                                    <img
                                        src={event.imageUrl || 'https://via.placeholder.com/150'}
                                        className='card-img-top'
                                        alt={event.eventName}
                                    />
                                    <div className='card-body'>
                                        <h5 className='card-title'>{event.eventName}</h5>
                                        <p className='card-text'><strong>Date:</strong> {new Date(event.eventDate).toLocaleDateString()}</p>
                                        <p className='card-text'><strong>Time:</strong> {formatTime(event.eventTime)}</p>
                                        <p className='card-text'><strong>Location:</strong> {event.eventLocation}</p>
                                        <p className='card-text'><strong>Description:</strong> {event.description}</p>
                                        <p className='card-text'><strong>Category:</strong> {event.eventCategory}</p>
                                        <p className='card-text'><strong>Price:</strong> ${event.eventPrice.toFixed(2)}</p>
                                        <Link to={`/events/${event.id}`} className='btn btn-primary'>View Details</Link>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className='mt-3'>
                        <button className='btn btn-secondary me-2' onClick={clearFilters}>Clear Filters</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Events;
