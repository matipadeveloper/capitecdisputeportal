FROM nginx

# Remove default Nginx configuration if it exists
RUN if [ -f /etc/nginx/conf.d/default.conf ]; then rm /etc/nginx/conf.d/default.conf; fi

# Copy custom home page
COPY index.html /etc/nginx/html/

# Set permissions for the custom home page
RUN chmod 0644 /etc/nginx/html/index.html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/

# Set the default command to start Nginx
CMD ["nginx", "-g", "daemon off;"]