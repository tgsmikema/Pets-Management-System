using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.OAuth;
using Microsoft.EntityFrameworkCore;
using SPCA_backend.Data;
using SPCA_backend.Handler;

// configure CORS setting
var MyAllowSpecificOrigins = "_myAllowSpecificOrigins";

var builder = WebApplication.CreateBuilder(args);

// configure CORS setting to accept incoming request from the react + vite frontend server.
builder.Services.AddCors(options =>
{
    options.AddPolicy(MyAllowSpecificOrigins,
        policy =>
        {
            policy.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod(); ;
        });
});

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

//builder.Services.AddDbContext<SPCA_DBContext>(options => options.UseSqlServer(builder.Configuration["WebAPIConnection"]));
builder.Services.AddDbContext<SPCA_DBContext>(options => options.UseSqlite(builder.Configuration["WebAPIConnection"]));
builder.Services.AddScoped<ISPCARepo, SPCARepo>();

//register an authentication scheme
builder.Services.AddAuthentication()
    .AddScheme<AuthenticationSchemeOptions, SPCAAuthHandler>("Authentication", null);


//register an authorization policy
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("AdminOnly",
                                    policy => policy.RequireClaim("admin"));
    options.AddPolicy("VetOnly",
                                    policy => policy.RequireClaim("vet"));
    options.AddPolicy("VolunteerOnly",
                                    policy => policy.RequireClaim("volunteer"));
    options.AddPolicy("AdminAndVetOnly", policy =>
    {
        policy.RequireAssertion(context => context.User.HasClaim(c =>
        (c.Type == "admin" || c.Type == "vet")));
    });
    options.AddPolicy("VetAndVolunteerOnly", policy =>
    {
        policy.RequireAssertion(context => context.User.HasClaim(c =>
        (c.Type == "vet" || c.Type == "volunteer")));
    });
    options.AddPolicy("AdminAndVolunteerOnly", policy =>
    {
        policy.RequireAssertion(context => context.User.HasClaim(c =>
        (c.Type == "admin" || c.Type == "volunteer")));
    });
    options.AddPolicy("AllUser", policy =>
    {
        policy.RequireAssertion(context => context.User.HasClaim(c =>
        (c.Type == "admin" || c.Type == "vet" || c.Type == "volunteer")));
    });

});



var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment() || app.Environment.IsProduction())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

//app.UseHttpsRedirection();

// configure CORS setting
app.UseCors(MyAllowSpecificOrigins);

//add authentication to the processing pipeline
app.UseAuthentication();

app.UseAuthorization();

app.MapControllers();

app.Run();
