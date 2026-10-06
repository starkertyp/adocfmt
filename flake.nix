{
  description = "adocfmt — AsciiDoc formatter CLI development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-26.05";
    git-hooks = {
      url = "github:cachix/git-hooks.nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      git-hooks,
    }:
    let
      systems = [
        "aarch64-darwin"
        "aarch64-linux"
        "x86_64-darwin"
        "x86_64-linux"
      ];

      hooks = {
        google-java-format.enable = true;
        check-merge-conflicts.enable = true;
        end-of-file-fixer.enable = true;
        mixed-line-endings.enable = true;
        trim-trailing-whitespace.enable = true;
        nixfmt.enable = true;
      };

      preCommitFor =
        pkgs:
        git-hooks.lib.${pkgs.system}.run {
          src = self;
          package = pkgs.prek;
          inherit hooks;
        };
    in
    {
      devShells = nixpkgs.lib.genAttrs systems (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          preCommit = preCommitFor pkgs;
          jdk = pkgs.jdk21;
        in
        {
          default = pkgs.mkShell {
            packages = [
              jdk
              pkgs.maven
              pkgs.jdt-language-server
              pkgs.google-java-format
              pkgs.opencode
              pkgs.openspec
            ]
            ++ preCommit.enabledPackages;
            JAVA_HOME = jdk.home;
            shellHook = preCommit.shellHook;
          };
        }
      );

      checks = nixpkgs.lib.genAttrs systems (system: {
        pre-commit-check = preCommitFor nixpkgs.legacyPackages.${system};
      });

      formatter = nixpkgs.lib.genAttrs systems (
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          preCommit = preCommitFor pkgs;
        in
        pkgs.writeShellScriptBin "format" ''
          export PATH="${
            nixpkgs.lib.makeBinPath (preCommit.enabledPackages ++ [ preCommit.config.package ])
          }:$PATH"
          exec ${nixpkgs.lib.getExe preCommit.config.package} run --all-files
        ''
      );
    };
}
