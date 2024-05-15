package s1riys.lab8.server.managers;

import org.apache.logging.log4j.Logger;
import s1riys.lab8.common.exceptions.ForbiddenException;
import s1riys.lab8.common.models.Coordinates;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.Organization;
import s1riys.lab8.common.models.User;
import s1riys.lab8.server.Main;
import s1riys.lab8.server.Services.ProductService;
import s1riys.lab8.server.Services.utils.ServiceLocator;
import s1riys.lab8.server.entities.ProductEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The CollectionManager class manages the collection of products, including serialization and deserialization.
 */
public class CollectionManager {
    private final HashMap<Long, Product> collection = new HashMap<>();
    private LocalDateTime initTime;
    private LocalDateTime saveTime;
    private final Logger logger = Main.logger;
    private final ProductService productService = (ProductService) ServiceLocator.getService("ProductService");
    private final boolean isRefreshing = true;
    private final Integer refreshingRate = 5_000;
    private final Object collectionLock = new Object();

    /**
     * Constructs a CollectionManager object with the specified DumpManager and initializes the collection.
     */
    public CollectionManager() {
        try {
            load();
            this.initTime = LocalDateTime.now();
        } catch (Exception e) {
            logger.fatal("Ошибка при загрузке данных из БД", e);
            System.exit(1);
        }

        if (!validateAll()) {
            logger.fatal("В БД есть невалидные записи");
            System.exit(1);
        }
    }

    /**
     * Retrieves the default collection as a HashMap.
     *
     * @return The default collection as a HashMap.
     */
    public HashMap<Long, Product> getCollection() {
        return collection;
    }


    /**
     * Retrieves the type of the collection.
     *
     * @return The type of the collection.
     */
    public String getCollectionType() {
        return "Product";
    }

    /**
     * Retrieves the size of the collection.
     *
     * @return The size of the collection.
     */
    public int getCollectionSize() {
        return collection.size();
    }

    /**
     * Retrieves the initialization date and time of the collection.
     *
     * @return The initialization date and time of the collection.
     */
    public LocalDateTime getInitDate() {
        return initTime;
    }

    /**
     * Updates the save time to the current date and time.
     */
    private void updateSaveTime() {
        this.saveTime = LocalDateTime.now();
    }

    /**
     * Retrieves the save time of the collection.
     *
     * @return The save time of the collection.
     */
    public LocalDateTime getSaveTime() {
        return saveTime;
    }

    /**
     * Loads data from database.
     */
    private void load() {
        productService.findAll().stream()
                .map((entity) -> {
                    Organization manufacturer = null;
                    if (entity.getManufacturer() != null) {
                        manufacturer = new Organization(
                                entity.getManufacturer().getId(),
                                entity.getManufacturer().getName(),
                                entity.getManufacturer().getFullName(),
                                entity.getManufacturer().getAnnualTurnover(),
                                entity.getManufacturer().getEmployeesCount()
                        );
                    }
                    return new Product(
                            entity.getId(),
                            entity.getName(),
                            new Coordinates(entity.getX(), entity.getY()),
                            entity.getCreationDate(),
                            entity.getPrice(),
                            entity.getPartNumber(),
                            entity.getUnitOfMeasure(),
                            manufacturer,
                            entity.getCreator().getId()
                    );
                })
                .forEach(product -> collection.put(product.getId(), product));
    }

    public boolean validateAll() {
        return getCollection().values().stream()
                .allMatch(Product::validate);
    }

    public Product getById(Long id) {
        return getCollection().get(id);
    }

    public List<Product> getSortedCollection() {
        return new ArrayList<>(collection.values())
                .stream()
                .sorted(Product::compareTo)
                .collect(Collectors.toList());
    }

    /**
     * Adds a product to the collection.
     *
     * @param product The product to add.
     */
    public Long add(Product product, User user) {
        Long nextProductId = productService.add(product, user);
        logger.info("Новый продукт добавлен в БД");

        Product processedProduct = product.copy(nextProductId, user.getId());

        synchronized (collectionLock) {
            collection.put(processedProduct.getId(), processedProduct);
            updateSaveTime();
        }

        logger.info("Новый продукт добавлен в коллекцию");

        System.out.println(collection);

        return nextProductId;
    }

    /**
     * Removes a product from the collection based on the specified ID.
     *
     * @param id The ID of the product to remove.
     */
    public int remove(Long id, User user) throws ForbiddenException {
        ProductEntity productToDelete = productService.findById(id);
        if (!Objects.equals(productToDelete.getCreator().getId(), user.getId())) throw new ForbiddenException();

        int removedCount = productService.removeById(id);

        synchronized (collectionLock) {
            collection.remove(id);
            updateSaveTime();
        }
        return removedCount;
    }

    /**
     * Updates a product in the collection based on the specified ID.
     *
     * @param id      The ID of the product to update.
     * @param product The updated product.
     */
    public void update(Long id, Product product, User user) throws ForbiddenException {
        ProductEntity productToUpdate = productService.findById(id);

        System.out.println(productToUpdate.getCreator().getId());
        System.out.println(user.getId());
        if (!Objects.equals(productToUpdate.getCreator().getId(), user.getId())) throw new ForbiddenException();
        else {
            productService.update(id, product);

            synchronized (collectionLock) {
                getById(id).update(product);
                updateSaveTime();
            }
        }
    }

    /**
     * Clears the collection, removing all products.
     */
    public void clear(User user) {
        List<Long> IdsToRemove = collection.values().stream()
                .filter(product -> Objects.equals(product.getCreatorId(), user.getId()))
                .map(Product::getId)
                .toList();

        for (Long id : IdsToRemove) collection.remove(id);

        synchronized (collectionLock) {
            productService.clear(user.getId());
            updateSaveTime();
        }
    }

    public void startRefreshing() {
        Thread refresher = new Thread(() -> {
            while (isRefreshing) {
                load();
                try {
                    Thread.sleep(refreshingRate);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread was interrupted, Failed to complete operation");
                }
            }
        });
        refresher.start();
    }
}
