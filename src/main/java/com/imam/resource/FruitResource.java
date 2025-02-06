package com.imam.resource;

import com.imam.entity.Fruit;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/fruits")
@ApplicationScoped
public class FruitResource {
    @GET
    public Uni<List<Fruit>> get(){
        return Fruit.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Uni<Fruit> getFruitById(Long id){
        return Fruit.findById(id);
    }

    @POST
    public Uni<RestResponse<Fruit>> create(Fruit fruit) {
        return Panache.withTransaction(() -> {
            Fruit newFruit = new Fruit();
            newFruit.name = fruit.name;
            return newFruit.persist();
        }).replaceWith(RestResponse.status(Response.Status.CREATED, fruit));
    }

    @PATCH
    @Path("/{id}")
    public Uni<RestResponse<Fruit>> updateFruit(@PathParam("id") Long id, Fruit updatedFruit) {
        return Panache.withTransaction(() -> Fruit.<Fruit>findById(id)
                .onItem().ifNull().failWith(new IllegalArgumentException("Fruit not found!"))
                .onItem().invoke(fruit -> {
                    fruit.name = updatedFruit.name;
                })
        ).replaceWith(RestResponse.ok(updatedFruit));
    }

    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<String>> delete(@PathParam("id") Long id) {
        return Panache.withTransaction(() -> Fruit.deleteById(id))
                .map(deleted -> deleted
                        ? RestResponse.status(Response.Status.OK, "Fruit deleted successfully!")
                        : RestResponse.status(Response.Status.NOT_FOUND, "Fruit not found!")
                );
    }
}
