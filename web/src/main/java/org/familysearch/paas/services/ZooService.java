package org.familysearch.paas.services;

import org.codehaus.enunciate.modules.jersey.ExternallyManagedLifecycle;
import org.familysearch.paas.db.EntityManagerFactoryFactory;
import org.familysearch.paas.db.entities.AnimalEntity;
import org.familysearch.paas.schema.Animal;
import org.familysearch.sas.client.ObjectRequester;
import org.familysearch.sas.schema.Attribute;
import org.familysearch.sas.schema.Sas;
import org.familysearch.ws.rt.WebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A very simple web service
 */
@Component
@Path("/zoo")
@ExternallyManagedLifecycle
public class ZooService {

  private ObjectRequester requester = new ObjectRequester();
  private EntityManagerFactory entityManagerFactory;
  private static final Logger LOG = LoggerFactory.getLogger(ZooService.class);

  @Autowired
  public ZooService(EntityManagerFactoryFactory entityManagerFactoryFactory) throws IOException {
    this.entityManagerFactory = entityManagerFactoryFactory.createEntityManagerFactory();

    if (getAnimals().size() == 0) {
      LOG.info("Zoo is empty; initializing the zoo with a few animals");
      addAnimal("kangaroo");
      addAnimal("elephant");
      addAnimal("tiger");
      addAnimal("zebra");
      addAnimal("cobra");
      addAnimal("moose");
    }
  }

  /**
   * Get the specified animal. It produces both "application/json" and "application/xml"
   * <p/>
   * <p/>
   * <pre>
   * <b>Error Response Codes</b>
   * <ul>
   *   <li>404 Not Found: No Animal exists with the specified name</li>
   * </ul>
   * </pre>
   *
   * @param name of the Animal to retrieve
   * @return The default Animal.
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("animals/{name}")
  public Animal getAnimal(@PathParam("name") final String name) throws WebServiceException {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<AnimalEntity> query = em.createQuery("select a from animal a where a.name = ?1", AnimalEntity.class);
      query.setParameter(1, name.toLowerCase());
      return new Animal(query.getSingleResult().getName());
    } catch (javax.persistence.NoResultException e) {
      throw new WebServiceException(e, Response.Status.NOT_FOUND.getStatusCode());
    } finally {
      em.close();
    }
  }

  /**
   * List all the animals
   *
   * @return A list of animals
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("animals")
  public List<Animal> getAnimals() {

    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<AnimalEntity> result = em.createQuery("select a from animal a", AnimalEntity.class).getResultList();
    List<Animal> animals = new ArrayList<Animal>();
    for (AnimalEntity animalEntity : result) {
      animals.add(new Animal(animalEntity.getName()));
    }
    em.getTransaction().commit();

    em.close();

    return animals;
  }


  /**
   * Throws an exception to demonstrate how exceptions can be handled.
   *
   * @param message The message for the exception.
   * @return nothing; an exception is thrown every time.
   * @throws WebServiceException always.
   */
  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("error")
  public Animal throwException(@QueryParam("message") String message) throws WebServiceException {
    if (message == null) {
      message = "The zoo is temporarily unavailable until we figure out where the tigers are.";
    }
    LOG.error("Sample error message");
    throw new WebServiceException(message);
  }

  /**
   * Add an animal
   *
   * @param name of the Animal to add
   * @return The animal that was added
   */
  @PUT
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("animals/{name}")
  public Animal addAnimal(@PathParam("name") final String name) {
    AnimalEntity animalEntity = new AnimalEntity(name.toLowerCase());
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      TypedQuery<AnimalEntity> query = em.createQuery("select a from animal a where a.name = ?1", AnimalEntity.class);
      query.setParameter(1, animalEntity.getName());
      List<AnimalEntity> matchingAnimalList = query.getResultList();
      if (matchingAnimalList.size() > 0) {
        animalEntity = matchingAnimalList.get(0);
        LOG.info(String.format("Updating animal %s (id: %s)", animalEntity.getName(), animalEntity.getId()));
        em.merge(animalEntity);
      } else {
        LOG.info(String.format("Adding animal %s to the zoo", animalEntity.getName()));
        em.persist(animalEntity);
      }
      em.getTransaction().commit();
      return new Animal(animalEntity.getName());
    } finally {
      em.close();
    }
  }

  /**
   * Remove an animal
   * <p/>
   * <p/>
   * <pre>
   * <b>Error Response Codes</b>
   * <ul>
   *   <li>404 Not Found: No Animal exists with the specified name</li>
   * </ul>
   * </pre>
   */
  @DELETE
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  @Path("animals/{name}")
  public void removeAnimal(@PathParam("name") final String name) throws WebServiceException {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      TypedQuery<AnimalEntity> query = em.createQuery("select a from animal a where a.name = ?1", AnimalEntity.class);
      query.setParameter(1, name.toLowerCase());
      List<AnimalEntity> matchingAnimalList = query.getResultList();
      if (matchingAnimalList.size() > 0) {
        AnimalEntity animalEntity = matchingAnimalList.get(0);
        LOG.info(String.format("Deleting animal %s (id: %s)", animalEntity.getName(), animalEntity.getId()));
        em.remove(animalEntity);
      }
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
