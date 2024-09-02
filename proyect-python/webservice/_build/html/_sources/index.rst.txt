.. Flask Shop Pokémon documentation master file, created by
   sphinx-quickstart on Thu Aug 22 23:45:33 2024.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Flask Shop Pokémon
===================

This project is a web application developed with Flask in Python that manages a Pokémon merchandise store. The application is designed to provide a smooth and efficient user experience in managing two main types of products: **figures** and **collectible cards**.

## Main Features

### Product Management

The application supports CRUD (Create, Read, Update, Delete) operations for the following types of products:

- **Figures**: You can add new figures, view existing ones, update their information, and delete outdated records.
- **Collectible Cards**: Similarly, you can manage collectible cards, ensuring that information is always accurate and up-to-date.

### Real-Time Queries

A notable feature of the application is its ability to perform real-time queries to an external API. This allows obtaining up-to-date data on Pokémon cards, ensuring that users always have access to the most current information about collectible cards.

### Firebase Integration

The application uses **Firebase** for real-time management of figure and card records. This means that any changes made to the database are instantly synchronized with the application, ensuring that data is always accurate and up-to-date. Firebase provides a robust solution for real-time storage and data synchronization.

### Developer Interface

Currently, the application does not include a detailed installation section but provides a link for interested developers. This link offers access to technical documentation and development guides to facilitate collaboration and extension of the project.

## Developer Link

For more information on development and contributions, as well as access to technical documentation, visit the following link:

[Developer Link](https://example.com/developers)

This link will provide access to the resources needed to understand and contribute to the development of the application.

.. toctree::
   :maxdepth: 2
   :caption: Contents:

   introduction
   installation
   usage
   api_reference
   firebase_integration
   troubleshooting
   contributing
