import React, { useState } from 'react';
import axios from 'axios';

const EventForm = () => {
    const [eventName, setEventName] = useState('');
    const [eventDate, setEventDate] = useState('');
    const [eventLocation, setEventLocation] = useState('');
    const [eventDescription, setEventDescription] = useState('');
    const [eventCategory, setEventCategory] = useState('');
    const [eventPrice, setEventPrice] = useState('');
    const [eventZipCode, setEventZipCode] = useState('');
    const [eventTime, setEventTime] = useState('');
    const [eventImage, setEventImage] = useState(null);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleFileChange = (e) => {
        setEventImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('eventName', eventName);
        formData.append('eventDate', eventDate);
        formData.append('eventLocation', eventLocation);
        formData.append('eventDescription', eventDescription);
        formData.append('eventCategory', eventCategory);
        formData.append('eventPrice', eventPrice);
        formData.append('eventZipCode', eventZipCode);
        formData.append('eventTime', eventTime);
        if (eventImage) formData.append('eventImage', eventImage);

        try {
            await axios.post('http://localhost:8080/api/events', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            setSuccess('Event created successfully!');
            // Reset form fields
            setEventName('');
            setEventDate('');
            setEventLocation('');
            setEventDescription('');
            setEventCategory('');
            setEventPrice('');
            setEventZipCode('');
            setEventTime('');
            setEventImage(null);
        } catch (err) {
            console.error('Error creating event:', err);
            setError('Failed to create event. Please try again.');
        }
    };

    return (
        <div className='container py-5'>
            <h1 className='text-primary'>Create Event</h1>
            {error && <div className="alert alert-danger">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}
            <form onSubmit={handleSubmit}>
                <div className='mb-3'>
                    <label htmlFor='eventName' className='form-label'>Event Name</label>
                    <input
                        type='text'
                        className='form-control'
                        id='eventName'
                        value={eventName}
                        onChange={(e) => setEventName(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventDate' className='form-label'>Event Date</label>
                    <input
                        type='date'
                        className='form-control'
                        id='eventDate'
                        value={eventDate}
                        onChange={(e) => setEventDate(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventTime' className='form-label'>Event Time</label>
                    <input
                        type='time'
                        className='form-control'
                        id='eventTime'
                        value={eventTime}
                        onChange={(e) => setEventTime(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventLocation' className='form-label'>Event Location</label>
                    <input
                        type='text'
                        className='form-control'
                        id='eventLocation'
                        value={eventLocation}
                        onChange={(e) => setEventLocation(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventZipCode' className='form-label'>Event Zip Code</label>
                    <input
                        type='text'
                        className='form-control'
                        id='eventZipCode'
                        value={eventZipCode}
                        onChange={(e) => setEventZipCode(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventDescription' className='form-label'>Event Description</label>
                    <textarea
                        className='form-control'
                        id='eventDescription'
                        rows='3'
                        value={eventDescription}
                        onChange={(e) => setEventDescription(e.target.value)}
                        required
                    ></textarea>
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventCategory' className='form-label'>Event Category</label>
                    <input
                        type='text'
                        className='form-control'
                        id='eventCategory'
                        value={eventCategory}
                        onChange={(e) => setEventCategory(e.target.value)}
                        required
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventPrice' className='form-label'>Event Price</label>
                    <input
                        type='number'
                        className='form-control'
                        id='eventPrice'
                        value={eventPrice}
                        onChange={(e) => setEventPrice(e.target.value)}
                        required
                        step='0.01'
                    />
                </div>
                <div className='mb-3'>
                    <label htmlFor='eventImage' className='form-label'>Event Image</label>
                    <input
                        type='file'
                        className='form-control'
                        id='eventImage'
                        onChange={handleFileChange}
                    />
                </div>
                <button type='submit' className='btn btn-primary'>Create Event</button>
            </form>
        </div>
    );
};

export default EventForm;
