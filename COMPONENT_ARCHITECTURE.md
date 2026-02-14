# PhotoConnect Component Architecture

## New File Structure

### CSS Files (New)
- `/css/navbar.css` - Navbar component styles with responsive design
- `/css/footer.css` - Footer component styles with responsive design  
- `/css/header.css` - Page header component styles with responsive design

### Component Files (Updated)
- `/components/navbar.html` - Responsive navbar with hamburger menu for mobile
- `/components/footer.html` - Responsive footer with links and company info
- `/components/header.html` - Dynamic page header with breadcrumbs

### Page Files (Updated)
- `/pages/contact_us.html` - Contact page using new component system

## Features

### Navbar Component
✅ Sticky navigation bar
✅ Hamburger menu for tablets (768px and below)
✅ Responsive layout for mobile
✅ Sign In / Log Out buttons
✅ Logo linking to home
✅ Active menu toggle functionality

### Footer Component
✅ Company information section
✅ Quick links organized in columns
✅ Responsive grid layout
✅ Email and phone links
✅ Copyright notice
✅ Mobile-friendly vertical stacking

### Header Component
✅ Dynamic page title (set via JavaScript)
✅ Subtitle text
✅ Breadcrumb navigation
✅ Gradient background
✅ Responsive font sizing

### Contact Page
✅ Uses all three components
✅ Clean two-column form layout
✅ Form fields with proper labels
✅ Responsive single column on mobile
✅ Image section for visual appeal

## Responsive Breakpoints

### Desktop (> 1200px)
- Full navbar with all links visible
- Two-column form layout
- Large images and spacing

### Tablet (768px - 1200px)
- Navbar with hamburger menu
- Single column form layout
- Adjusted padding and margins

### Mobile (< 480px)
- Full hamburger menu for navigation
- Single column form layout
- Optimized spacing and font sizes
- Stacked footer sections

## Usage

### Loading Components in a Page
```html
<!-- HTML -->
<div id="navbar-container"></div>
<div id="header-container"></div>
<div id="footer-container"></div>

<!-- JavaScript -->
<script>
  fetch('/components/navbar.html')
    .then(r => r.text())
    .then(html => document.getElementById('navbar-container').innerHTML = html);

  fetch('/components/header.html')
    .then(r => r.text())
    .then(html => {
      document.getElementById('header-container').innerHTML = html;
      setPageHeader('Page Title', 'Subtitle', [
        { label: 'Home', url: '/index.html' },
        { label: 'Current Page', url: null }
      ]);
    });

  fetch('/components/footer.html')
    .then(r => r.text())
    .then(html => document.getElementById('footer-container').innerHTML = html);
</script>
```

## Benefits

1. **Reusability** - Use navbar, header, footer on every page
2. **Consistency** - Same styling across all pages
3. **Maintainability** - Update component once, applies everywhere
4. **Responsive** - Built-in responsive design for all breakpoints
5. **Modularity** - Easy to modify individual components
6. **Clean Code** - Separated concerns (components vs page-specific styles)
