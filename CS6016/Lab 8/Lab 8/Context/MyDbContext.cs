using System;
using System.Collections.Generic;
using Lab_8.Entities;
using Microsoft.EntityFrameworkCore;

namespace Lab_8.Context;

public partial class MyDbContext : DbContext
{
    public MyDbContext()
    {
    }

    public MyDbContext(DbContextOptions<MyDbContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Checkedout> Checkedouts { get; set; }

    public virtual DbSet<Inventory> Inventories { get; set; }

    public virtual DbSet<Patron> Patrons { get; set; }

    public virtual DbSet<Phone> Phones { get; set; }

    public virtual DbSet<Title> Titles { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see https://go.microsoft.com/fwlink/?LinkId=723263.
        => optionsBuilder.UseNpgsql("Host=atr.eng.utah.edu;Database=library_u1150859;Username=u1150859;");

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Checkedout>(entity =>
        {
            entity.HasKey(e => e.Serial).HasName("checkedout_pkey");

            entity.ToTable("checkedout");

            entity.HasIndex(e => e.Cardnum, "checkedout_cardnum_idx");

            entity.Property(e => e.Serial)
                .ValueGeneratedNever()
                .HasColumnName("serial");
            entity.Property(e => e.Cardnum).HasColumnName("cardnum");

            entity.HasOne(d => d.CardnumNavigation).WithMany(p => p.Checkedouts)
                .HasForeignKey(d => d.Cardnum)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("checkedout_ibfk_1");

            entity.HasOne(d => d.SerialNavigation).WithOne(p => p.Checkedout)
                .HasForeignKey<Checkedout>(d => d.Serial)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("checkedout_ibfk_2");
        });

        modelBuilder.Entity<Inventory>(entity =>
        {
            entity.HasKey(e => e.Serial).HasName("inventory_pkey");

            entity.ToTable("inventory");

            entity.HasIndex(e => e.Isbn, "Inventory_ISBN_idx");

            entity.Property(e => e.Serial).HasColumnName("serial");
            entity.Property(e => e.Isbn)
                .HasMaxLength(14)
                .IsFixedLength()
                .HasColumnName("isbn");

            entity.HasOne(d => d.IsbnNavigation).WithMany(p => p.Inventories)
                .HasForeignKey(d => d.Isbn)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("inventory_ibfk_1");
        });

        modelBuilder.Entity<Patron>(entity =>
        {
            entity.HasKey(e => e.Cardnum).HasName("patrons_pkey");

            entity.ToTable("patrons");

            entity.Property(e => e.Cardnum).HasColumnName("cardnum");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
        });

        modelBuilder.Entity<Phone>(entity =>
        {
            entity.HasKey(e => new { e.Cardnum, e.Phone1 }).HasName("phones_pkey");

            entity.ToTable("phones");

            entity.Property(e => e.Cardnum)
                .ValueGeneratedOnAdd()
                .HasColumnName("cardnum");
            entity.Property(e => e.Phone1)
                .HasMaxLength(8)
                .IsFixedLength()
                .HasColumnName("phone");

            entity.HasOne(d => d.CardnumNavigation).WithMany(p => p.Phones)
                .HasForeignKey(d => d.Cardnum)
                .HasConstraintName("phones_ibfk_1");
        });

        modelBuilder.Entity<Title>(entity =>
        {
            entity.HasKey(e => e.Isbn).HasName("titles_pkey");

            entity.ToTable("titles");

            entity.Property(e => e.Isbn)
                .HasMaxLength(14)
                .IsFixedLength()
                .HasColumnName("isbn");
            entity.Property(e => e.Author)
                .HasMaxLength(100)
                .HasColumnName("author");
            entity.Property(e => e.Title1)
                .HasMaxLength(100)
                .HasColumnName("title");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
