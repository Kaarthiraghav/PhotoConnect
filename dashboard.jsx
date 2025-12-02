import React, { useState } from 'react';
import { LayoutDashboard, Camera, Users, Image, Calendar, DollarSign, TrendingUp, MessageSquare, FileText, Clock, CheckCircle, XCircle, Instagram, Mail, Phone } from 'lucide-react';

const PhotoConnectDashboard = () => {
  const [activeRole, setActiveRole] = useState('admin');

  // Header Component
  const Header = () => (
    <nav className="bg-white shadow-sm">
      <div className="max-w-7xl mx-auto px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-gray-900 rounded flex items-center justify-center text-white font-bold text-lg">
              PC
            </div>
            <span className="text-xl font-semibold">PhotoConnect</span>
          </div>
          <div className="hidden md:flex gap-8 text-sm">
            <a href="#" className="hover:text-gray-600">Home</a>
            <a href="#" className="hover:text-gray-600">Photographers</a>
            <a href="#" className="hover:text-gray-600">About us</a>
            <a href="#" className="hover:text-gray-600">Contact Us</a>
          </div>
          <button className="bg-gray-900 text-white px-5 py-2 rounded text-sm hover:bg-gray-800">
            Sign in
          </button>
        </div>
      </div>
    </nav>
  );

  // Footer Component
  const Footer = () => (
    <footer className="bg-gray-900 text-white mt-16">
      <div className="max-w-7xl mx-auto px-6 py-12">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
          {/* Column 1: Brand and Contact */}
          <div>
            <h3 className="text-2xl font-bold mb-6">PhotoConnect</h3>
            <div className="space-y-3 text-sm">
              <div className="flex items-center gap-3">
                <Instagram className="w-5 h-5" />
                <span>@PhotoConnect</span>
              </div>
              <div className="flex items-center gap-3">
                <Mail className="w-5 h-5" />
                <span>photo.connect@gmail.com</span>
              </div>
              <div className="flex items-center gap-3">
                <Phone className="w-5 h-5" />
                <span>+91 777 - 123 - 123</span>
              </div>
            </div>
          </div>

          {/* Column 2: Main Navigation */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Navigation</h4>
            <ul className="space-y-3 text-sm">
              <li><a href="#" className="hover:text-gray-300 transition-colors">Home</a></li>
              <li><a href="#" className="hover:text-gray-300 transition-colors">Photographers</a></li>
              <li><a href="#" className="hover:text-gray-300 transition-colors">About Us</a></li>
              <li><a href="#" className="hover:text-gray-300 transition-colors">Contact Us</a></li>
            </ul>
          </div>

          {/* Column 3: Additional Links */}
          <div>
            <h4 className="text-lg font-semibold mb-4">More</h4>
            <ul className="space-y-3 text-sm">
              <li><a href="#" className="hover:text-gray-300 transition-colors">Become a Photographer</a></li>
              <li><a href="#" className="hover:text-gray-300 transition-colors">Careers</a></li>
            </ul>
          </div>
        </div>
      </div>
    </footer>
  );

  const StatCard = ({ icon: Icon, title, value, borderColor, iconBgColor, iconColor }) => (
    <div className="bg-white rounded-lg shadow-md p-6 border-l-4 hover:shadow-lg transition-shadow" style={{ borderColor: borderColor }}>
      <div className="flex items-center justify-between">
        <div>
          <p className="text-gray-600 text-sm mb-1">{title}</p>
          <h3 className="text-3xl font-bold text-gray-900">{value}</h3>
        </div>
        <div className="w-14 h-14 rounded-full flex items-center justify-center" style={{ backgroundColor: iconBgColor }}>
          <Icon className="w-7 h-7" style={{ color: iconColor }} />
        </div>
      </div>
    </div>
  );

  const AdminDashboard = () => (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Header />
      
      <div className="flex-grow">
        <div className="max-w-7xl mx-auto px-6 py-8">
          <div className="mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-2">Admin Dashboard</h2>
            <p className="text-gray-600">Manage your PhotoConnect platform</p>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <StatCard 
              icon={Users} 
              title="Total Users" 
              value="2,847" 
              borderColor="#a855f7"
              iconBgColor="#f3e8ff"
              iconColor="#7c3aed"
            />
            <StatCard 
              icon={Camera} 
              title="Photographers" 
              value="156" 
              borderColor="#3b82f6"
              iconBgColor="#dbeafe"
              iconColor="#2563eb"
            />
            <StatCard 
              icon={Image} 
              title="Total Photos" 
              value="45.2K" 
              borderColor="#ec4899"
              iconBgColor="#fce7f3"
              iconColor="#db2777"
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
            {/* Pending Photographer Requests */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <div className="flex items-center justify-between mb-6">
                <h3 className="text-xl font-bold text-gray-900 flex items-center gap-2">
                  <Clock className="w-5 h-5 text-orange-600" />
                  Pending Photographer Requests
                </h3>
                <span className="bg-orange-100 text-orange-800 text-xs font-semibold px-3 py-1 rounded-full">8 New</span>
              </div>
              <div className="space-y-4">
                {[
                  { name: 'Sarah Mitchell', type: 'Wedding Photography', date: '2 hours ago' },
                  { name: 'Mike Johnson', type: 'Portrait Photography', date: '5 hours ago' },
                  { name: 'Emma Davis', type: 'Event Photography', date: '1 day ago' },
                  { name: 'Alex Chen', type: 'Product Photography', date: '1 day ago' }
                ].map((request, i) => (
                  <div key={i} className="border border-gray-200 rounded-lg p-4">
                    <div className="mb-3">
                      <h4 className="font-semibold text-gray-900">{request.name}</h4>
                      <p className="text-sm text-gray-600">{request.type}</p>
                      <p className="text-xs text-gray-400 mt-1">{request.date}</p>
                    </div>
                    <div className="flex gap-2">
                      <button className="flex-1 bg-green-600 text-white px-3 py-2 rounded text-sm font-medium hover:bg-green-700 flex items-center justify-center gap-1">
                        <CheckCircle className="w-4 h-4" />
                        Approve
                      </button>
                      <button className="flex-1 bg-red-600 text-white px-3 py-2 rounded text-sm font-medium hover:bg-red-700 flex items-center justify-center gap-1">
                        <XCircle className="w-4 h-4" />
                        Reject
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Revenue Overview */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-green-600" />
                Revenue Overview
              </h3>
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">This Month</span>
                  <span className="text-3xl font-bold text-green-600">$34,567</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-3">
                  <div className="bg-green-600 h-3 rounded-full" style={{width: '78%'}}></div>
                </div>
                <div className="flex justify-between text-sm text-gray-500">
                  <span>Goal: $45,000</span>
                  <span>78% Complete</span>
                </div>
                <div className="pt-4 space-y-2 border-t border-gray-200">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">Last Month</span>
                    <span className="font-semibold text-gray-900">$31,245</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">This Year</span>
                    <span className="font-semibold text-gray-900">$387,920</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Reports */}
          <div className="bg-white rounded-lg shadow-md p-6">
            <h3 className="text-xl font-bold text-gray-900 mb-4 flex items-center gap-2">
              <FileText className="w-5 h-5 text-blue-600" />
              Recent Reports
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {['User Activity Report', 'Photo Upload Statistics', 'Payment Analytics'].map((report, i) => (
                <div key={i} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow cursor-pointer">
                  <span className="text-gray-700 font-medium">{report}</span>
                  <p className="text-blue-600 text-sm font-semibold mt-2">View →</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );

  const PhotoDashboard = () => (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Header />
      
      <div className="flex-grow">
        <div className="max-w-7xl mx-auto px-6 py-8">
          <div className="mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-2">Photographer Dashboard</h2>
            <p className="text-gray-600">Manage your photography business</p>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
            <StatCard 
              icon={Image} 
              title="My Photos" 
              value="1,234" 
              borderColor="#06b6d4"
              iconBgColor="#cffafe"
              iconColor="#0891b2"
            />
            <StatCard 
              icon={Calendar} 
              title="Events" 
              value="23" 
              borderColor="#f97316"
              iconBgColor="#ffedd5"
              iconColor="#ea580c"
            />
            <StatCard 
              icon={DollarSign} 
              title="Earnings" 
              value="$4,567" 
              borderColor="#10b981"
              iconBgColor="#d1fae5"
              iconColor="#059669"
            />
            <StatCard 
              icon={Users} 
              title="Clients" 
              value="67" 
              borderColor="#6366f1"
              iconBgColor="#e0e7ff"
              iconColor="#4f46e5"
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Profile */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <Camera className="w-5 h-5 text-cyan-600" />
                Photo Profile
              </h3>
              <div className="space-y-6">
                <div className="flex items-center gap-4">
                  <div className="w-20 h-20 bg-cyan-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
                    JD
                  </div>
                  <div>
                    <h4 className="font-bold text-lg text-gray-900">John Photographer</h4>
                    <p className="text-gray-600 text-sm">Wedding & Events Specialist</p>
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="text-center p-4 bg-gray-50 rounded-lg border border-gray-200">
                    <p className="text-2xl font-bold text-cyan-600">4.9</p>
                    <p className="text-sm text-gray-600">Rating</p>
                  </div>
                  <div className="text-center p-4 bg-gray-50 rounded-lg border border-gray-200">
                    <p className="text-2xl font-bold text-cyan-600">234</p>
                    <p className="text-sm text-gray-600">Reviews</p>
                  </div>
                </div>
                <button className="w-full bg-gray-900 text-white py-3 rounded font-medium hover:bg-gray-800">
                  Edit Profile
                </button>
              </div>
            </div>

            {/* Chat & Availability */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <MessageSquare className="w-5 h-5 text-orange-600" />
                Chat & Availability
              </h3>
              <div className="space-y-4">
                <div className="p-4 bg-orange-50 rounded-lg border border-orange-200">
                  <p className="font-semibold text-orange-800 mb-2">Upcoming Shoot</p>
                  <p className="text-sm text-gray-700">Wedding Photography</p>
                  <p className="text-sm text-gray-600">Tomorrow, 2:00 PM</p>
                </div>
                <button className="w-full bg-gray-900 text-white py-3 rounded font-medium hover:bg-gray-800">
                  Open Chat
                </button>
                <button className="w-full border-2 border-gray-900 text-gray-900 py-3 rounded font-medium hover:bg-gray-50">
                  Update Availability
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );

  const ClientDashboard = () => (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <Header />
      
      <div className="flex-grow">
        <div className="max-w-7xl mx-auto px-6 py-8">
          <div className="mb-8">
            <h2 className="text-3xl font-bold text-gray-900 mb-2">Client Dashboard</h2>
            <p className="text-gray-600">View your photos and manage bookings</p>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <StatCard 
              icon={Image} 
              title="My Photos" 
              value="456" 
              borderColor="#f43f5e"
              iconBgColor="#ffe4e6"
              iconColor="#e11d48"
            />
            <StatCard 
              icon={Calendar} 
              title="Events Booked" 
              value="3" 
              borderColor="#14b8a6"
              iconBgColor="#ccfbf1"
              iconColor="#0d9488"
            />
            <StatCard 
              icon={DollarSign} 
              title="Payments" 
              value="$2,340" 
              borderColor="#8b5cf6"
              iconBgColor="#ede9fe"
              iconColor="#7c3aed"
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* My Events */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <LayoutDashboard className="w-5 h-5 text-rose-600" />
                My Dashboard
              </h3>
              <div className="space-y-3">
                {[
                  { label: 'Wedding Album', status: 'Ready', color: 'bg-green-100 text-green-800' },
                  { label: 'Birthday Party', status: 'Processing', color: 'bg-yellow-100 text-yellow-800' },
                  { label: 'Corporate Event', status: 'Scheduled', color: 'bg-blue-100 text-blue-800' }
                ].map((item, i) => (
                  <div key={i} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
                    <div className="flex items-center justify-between">
                      <span className="font-medium text-gray-900">{item.label}</span>
                      <span className={`${item.color} px-3 py-1 rounded-full text-xs font-semibold`}>
                        {item.status}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Chat */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <MessageSquare className="w-5 h-5 text-teal-600" />
                Chat with Photographer
              </h3>
              <div className="space-y-4">
                <div className="p-4 bg-teal-50 rounded-lg border border-teal-200">
                  <p className="font-semibold text-teal-800 mb-2">Next Event</p>
                  <p className="text-sm text-gray-700">Family Photoshoot</p>
                  <p className="text-sm text-gray-600">Dec 15, 2024 at 10:00 AM</p>
                </div>
                <button className="w-full bg-gray-900 text-white py-3 rounded font-medium hover:bg-gray-800">
                  Message Photographer
                </button>
                <button className="w-full border-2 border-gray-900 text-gray-900 py-3 rounded font-medium hover:bg-gray-50">
                  View Event Pictures
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );

  return (
    <div>
      {/* Role Selector - Fixed at top */}
      <div className="bg-gradient-to-r from-purple-600 via-blue-600 to-pink-600 shadow-lg sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex gap-3">
            <button
              onClick={() => setActiveRole('admin')}
              className={`px-6 py-2.5 rounded-lg font-semibold text-sm transition-all ${
                activeRole === 'admin'
                  ? 'bg-white text-purple-600 shadow-lg scale-105'
                  : 'bg-white bg-opacity-20 text-white hover:bg-opacity-30'
              }`}
            >
              👨‍💼 Admin Panel
            </button>
            <button
              onClick={() => setActiveRole('photo')}
              className={`px-6 py-2.5 rounded-lg font-semibold text-sm transition-all ${
                activeRole === 'photo'
                  ? 'bg-white text-blue-600 shadow-lg scale-105'
                  : 'bg-white bg-opacity-20 text-white hover:bg-opacity-30'
              }`}
            >
              📸 Photographer Panel
            </button>
            <button
              onClick={() => setActiveRole('client')}
              className={`px-6 py-2.5 rounded-lg font-semibold text-sm transition-all ${
                activeRole === 'client'
                  ? 'bg-white text-pink-600 shadow-lg scale-105'
                  : 'bg-white bg-opacity-20 text-white hover:bg-opacity-30'
              }`}
            >
              👤 Client Panel
            </button>
          </div>
        </div>
      </div>

      {/* Dashboard Content */}
      {activeRole === 'admin' && <AdminDashboard />}
      {activeRole === 'photo' && <PhotoDashboard />}
      {activeRole === 'client' && <ClientDashboard />}
    </div>
  );
};

export default PhotoConnectDashboard;